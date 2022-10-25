package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.vo.AuthoritiesVo;
import lombok.val;

/**
 * Authorityリソース
 *
 * @author : [AOT] g.kim
 * @createdAt : 2022-10-17
 * @updatedAt : 2022-10-17
 */
@Singleton
@Service
public class AuthoritiesResources {

  private AuthoritiesResources self;
  private final ApplicationContext applicationContext;
  private final CommonMapper commonMapper;

  @Autowired
  TenantResources tenantResources;

  @SuppressWarnings("rawtypes")
  @Autowired
  RedisTemplate redisTemplate;

  AuthoritiesResources(ApplicationContext applicationContext, CommonMapper commonMapper) {
    this.applicationContext = applicationContext;
    this.commonMapper = commonMapper;
  }

  /**
   * 初期化する
   *
   * @author : [AOT] g.kim
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @createdAt : 2021-06-23
   * @updatedAt : 2021-06-23
   */
  @Lock(LockType.WRITE)
  @EventListener(ApplicationReadyEvent.class)
  public void initialize() throws JsonMappingException, JsonProcessingException {

    self = applicationContext.getBean(AuthoritiesResources.class);
    for (val tenant : tenantResources.getAll()) {
      self.initialize(tenant.getId());
    }
  }

  /**
   * テナントの初期化
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-13
   * @updatedAt : 2021-08-13
   * @param tenantId
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public void initialize(Integer tenantId) throws JsonMappingException, JsonProcessingException {
    self.get(tenantId);
  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  @Cacheable(key = "{ #tenantId }", value = "AuthorityResources::get")
  public Map<String, Map<Integer, Map<String, AuthoritiesVo>>> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    val authorities = commonMapper.selectAuthorities(tenantId);
    if (authorities != null && authorities.size() != 0) {
      val resultOptional = authorities.stream()
          .collect(Collectors.groupingBy(AuthoritiesVo::getRoleId,
              Collectors.groupingBy(AuthoritiesVo::getApiYn,
                  Collectors.toMap(AuthoritiesVo::getFunctionId, Function.identity()))));
      return resultOptional;
    }
    return null;
  }

  /**
   * 機能ID取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-10-21
   * @updatedAt : 2022-10-21
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public List<String> getAuthorityFunctionIds(Integer tenantId, List<String> roleIds, boolean apiYn)
      throws JsonMappingException, JsonProcessingException {
    val ids = new ArrayList<String>();
    for (val role : roleIds) {
      val functionIdSet = self.get(tenantId).get(role).get(apiYn ? 1 : 0).keySet();
      if (functionIdSet != null) {
        ids.addAll(functionIdSet);
      }
    }
    return ids;
  }

  /**
   * 機能ID確認
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-10-21
   * @updatedAt : 2022-10-21
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public boolean isAuthorityFunctionIds(Integer tenantId, List<String> roleIds, boolean apiYn,
      String functionId) throws JsonMappingException, JsonProcessingException {
    for (val role : roleIds) {
      if (self.get(tenantId).get(role).get(apiYn ? 1 : 0).get(functionId) != null) {
        return true;
      }
    }
    return false;
  }
}
