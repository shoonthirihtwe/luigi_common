package jp.co.ichain.luigi2.resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeCommon.FlagCode;
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
  public List<AuthoritiesVo> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    return commonMapper.selectAuthorities(tenantId);
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
  public List<String> getAuthorityFunctionIds(Integer tenantId, List<String> roles, boolean apiYn)
      throws JsonMappingException, JsonProcessingException {
    val authorities = self.getAuthorities(tenantId, apiYn);
    val functionList = new ArrayList<String>();
    for (val role : roles) {
      functionList.addAll(authorities.stream().filter(vo -> vo.getRoleId().equals(role))
          .map(vo -> vo.getFunctionId()).collect(Collectors.toList()));
    }
    return functionList;

  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-03-17
   * @updatedAt : 2022-03-17
   * @param tenantId
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */

  public Date getLastUpdatedAt(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {

    if (self == null) {
      initialize();
    }

    // last updatedAt
    return self.get(tenantId).stream()
        .map(vo -> vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt())
        .max(Comparator.comparing(updatedAt -> updatedAt.getTime())).orElse(null);
  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-01
   * @updatedAt : 2021-10-01
   * @param tenantId
   * @param key
   * @param codeValue
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public List<AuthoritiesVo> getAuthorities(Integer tenantId, boolean apiYn)
      throws JsonMappingException, JsonProcessingException {

    val list = self.get(tenantId);
    Integer apiFlag = apiYn ? Integer.parseInt(FlagCode.TRUE.toString())
        : Integer.parseInt(FlagCode.FALSE.toString());
    if (list != null) {
      List<AuthoritiesVo> resultOptional =
          list.stream().filter(vo -> apiFlag.equals(vo.getApiYn())).collect(Collectors.toList());
      return resultOptional;
    }
    return null;
  }

  /**
   * リソースを更新する
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-10-21
   * @updatedAt : 2022-10-21
   */
  public void refresh() {
    val updatedAtResult = commonMapper.selectAuthoritiesLastUpdatedAt();
    updatedAtResult.forEach(item -> {
      val tenantId = ((Long) item.get("tenantId")).intValue();

      try {
        val updatedAt = self.getLastUpdatedAt(tenantId);
        if (updatedAt == null || updatedAt.getTime() < (long) item.get("updatedAt")) {
          // clear tenant redis data
          RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
          Set<byte[]> keys = conn.keys(("AuthorityResources::get::" + tenantId).getBytes());
          for (val n : keys) {
            conn.del(n);
          }
          self.initialize(tenantId);
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
          System.out
              .println("[Authorities Refresh] Authorities Refresh : " + sdf.format(new Date()));
        }
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });
  }

}
