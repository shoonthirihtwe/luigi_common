package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import lombok.val;

/**
 * 画面構成リソース
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-29
 * @updatedAt : 2021-07-29
 */
@Singleton
@Service
public class FrontDesignResources {

  private FrontDesignResources self;
  private final ApplicationContext applicationContext;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

  FrontDesignResources(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * メッセージリソースを初期化する
   *
   * @author : [AOT] s.paku
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   */
  @Lock(LockType.WRITE)
  @EventListener(ApplicationReadyEvent.class)
  public void initialize() throws JsonMappingException, JsonProcessingException {

    self = applicationContext.getBean(FrontDesignResources.class);

    val tenantIdList = serviceInstancesResources.getTenantList();

    for (val tenantId : tenantIdList) {
      self.initialize(tenantId);
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
    self.getByTenantId(tenantId);
  }

  /**
   * メッセージリソースを初期化する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-17
   * @updatedAt : 2021-08-17
   * @param tenantId
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public Date getLastUpdatedAt(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    return serviceInstancesResources.get(tenantId).entrySet().stream()
        .filter(entry -> entry.getKey().contains("_front")).map(entry -> {
          val vo = entry.getValue().get(0);
          return vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt();
        }).max(Comparator.comparing(updatedAt -> updatedAt.getTime()))
        .orElseThrow(() -> new WebDataException(Luigi2ErrorCode.D0002));
  }

  /**
   * テナント別情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @param tenantId
   * @param updatedAt
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public Map<String, Map<String, Object>> get(Integer tenantId, Long updatedAt)
      throws JsonMappingException, JsonProcessingException {

    return (updatedAt != null && updatedAt > self.getLastUpdatedAt(tenantId).getTime() ? null
        : self.get(tenantId));

  }

  @Cacheable(key = "{ #tenantId }", value = "FrontDesignResources::getByTenantId")
  public Map<String, Map<String, Object>> getByTenantId(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    return serviceInstancesResources.get(tenantId).entrySet().stream()
        .filter(entry -> entry.getKey().contains("_front")).map(entry -> entry.getValue().get(0))
        .collect(
            Collectors.toMap(ServiceInstancesVo::getSourceKey, ServiceInstancesVo::getInherentMap));
  }

  /**
   * テナント別情報取得
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Map<String, Map<String, Object>> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {

    return self.getByTenantId(tenantId);
  }

  /**
   * 全項目取得
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public List<Map<String, Map<String, Object>>> getAll()
      throws JsonMappingException, JsonProcessingException {

    val tenantIdList = serviceInstancesResources.getTenantList();
    val resultList = new ArrayList<Map<String, Map<String, Object>>>();
    for (val tenantId : tenantIdList) {
      resultList.add(self.getByTenantId(tenantId));
    }

    return resultList;
  }

}
