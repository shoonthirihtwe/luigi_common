package jp.co.ichain.luigi2.resources;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.util.StringUtils;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import lombok.val;

/**
 * Service Instancesリソース
 *
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-07
 * @updatedAt : 2021-05-07
 */
@Singleton
@Service
@DependsOn(value = {"dataSourceInitializer"})
public class ServiceInstancesResources {

  private ServiceInstancesResources self;
  private final ApplicationContext applicationContext;
  private final CommonMapper commonMapper;

  @Value("${business.group.type}")
  private String businessGroupType;

  ServiceInstancesResources(ApplicationContext applicationContext, CommonMapper commonMapper) {
    this.applicationContext = applicationContext;
    this.commonMapper = commonMapper;
  }

  /**
   * 初期化する
   *
   * @author : [AOT] s.paku
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   */
  @Lock(LockType.WRITE)
  @Order(value = 0)
  @EventListener(ApplicationReadyEvent.class)
  public void initialize() throws JsonMappingException, JsonProcessingException {

    self = applicationContext.getBean(ServiceInstancesResources.class);

    val list = commonMapper.selectServiceInstances();
    Map<Integer, List<ServiceInstancesVo>> tenantGroupMap =
        list.stream().collect(Collectors.groupingBy(vo -> vo.getTenantId()));

    for (val tenantId : tenantGroupMap.keySet()) {
      self.initialize(tenantId);
    }
  }

  /**
   * テナントの初期化
   * 
   * @author : [AOT] s.paku
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
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  @SuppressWarnings("unchecked")
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getByTenantId")
  public Map<String, List<ServiceInstancesVo>> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {

    val list = commonMapper.selectServiceInstances(tenantId);

    // json map setting
    for (val vo : list) {
      if (StringUtils.isEmpty(vo.getInherentJson()) == false) {
        ObjectMapper mapper = new ObjectMapper();
        if (vo.getInherentJson().charAt(0) == '[') {
          vo.setInherentList(mapper.readValue(vo.getInherentJson(), List.class));
        } else {
          vo.setInherentMap(mapper.readValue(vo.getInherentJson(), Map.class));
        }
      }
    }

    return list.stream().filter(
        x -> (x.getBusinessGroupType() == null) || (x.getBusinessGroupType() == businessGroupType))
        .collect(Collectors.groupingBy(vo -> vo.getSourceKey()));
  }

  /**
   * 情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param tenantId
   * @param sourceKey
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  @Cacheable(key = "{ #sourceKey+'::'+#tenantId }",
      value = "ServiceInstancesResources::getByTenantIdWithSourceKey")
  public List<ServiceInstancesVo> get(Integer tenantId, String sourceKey)
      throws JsonMappingException, JsonProcessingException {

    return self.get(tenantId) != null ? self.get(tenantId).get(sourceKey) : null;
  }

  /**
   * 情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param tenantId
   * @param sourceKey
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Optional<ServiceInstancesVo> getFirst(Integer tenantId, String sourceKey)
      throws JsonMappingException, JsonProcessingException {

    val result = self.get(tenantId).get(sourceKey);

    return result != null ? result.stream().findFirst() : null;
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
  @Cacheable(value = "ServiceInstancesResources::getTenantList")
  public Set<Integer> getTenantList() throws JsonMappingException, JsonProcessingException {
    val list = commonMapper.selectServiceInstances();
    return list.stream().filter(
        x -> (x.getBusinessGroupType() == null) || (x.getBusinessGroupType() == businessGroupType))
        .map(vo -> vo.getTenantId()).collect(Collectors.toSet());

  }

  /**
   * 最新更新日取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param tenantId
   * @return
   */
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getUpdatedAt")
  public Date getUpdatedAt(Integer tenantId) {

    val list = commonMapper.selectServiceInstances();

    // last updatedAt
    return list.stream()
        .filter(x -> (x.getBusinessGroupType() == null)
            || (x.getBusinessGroupType() == businessGroupType))
        .map(vo -> vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt())
        .max(Comparator.comparing(updatedAt -> updatedAt.getTime()))
        .orElseThrow(() -> new WebDataException(Luigi2ErrorCode.D0002));
  }

  /**
   * 最後更新日以降取得
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param updatedAt
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Map<String, List<ServiceInstancesVo>> getAllLastUpdatedDateAfter(Integer tenantId,
      Date updatedAt) throws JsonMappingException, JsonProcessingException {

    Date lastUpdatedAt = self.getUpdatedAt(tenantId);
    val mapByTenant = self.get(tenantId);

    if (updatedAt == null || lastUpdatedAt == null
        || updatedAt.getTime() == lastUpdatedAt.getTime()) {
      return this.get(tenantId);
    } else if (updatedAt.getTime() < lastUpdatedAt.getTime()) {
      val result = new HashMap<String, List<ServiceInstancesVo>>();
      for (val key : mapByTenant.keySet()) {
        val list = mapByTenant.get(key);
        result.put(key,
            list.stream().filter(vo -> updatedAt.getTime() < vo.getUpdatedAt().getTime())
                .collect(Collectors.toList()));
      }
      return result;
    } else {
      throw new WebException(Luigi2ErrorCode.S0000);
    }
  }
}
