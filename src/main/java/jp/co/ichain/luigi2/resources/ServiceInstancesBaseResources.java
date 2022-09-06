package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
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
import org.json.JSONObject;
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
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.util.StringUtils;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import lombok.val;

/**
 * Service Instances Baseリソース
 *
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-07
 * @updatedAt : 2021-05-07
 */
@Singleton
@Service
@DependsOn(value = {"dataSourceInitializer"})
public class ServiceInstancesBaseResources {

  private ServiceInstancesBaseResources self;
  private final ApplicationContext applicationContext;
  private final CommonMapper commonMapper;

  @Value("${business.group.type}")
  private String businessGroupType = null;

  ServiceInstancesBaseResources(ApplicationContext applicationContext, CommonMapper commonMapper) {
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

    self = applicationContext.getBean(ServiceInstancesBaseResources.class);

    val list = commonMapper.selectServiceInstancesBaseData(null, null);
    Map<Integer, List<ServiceInstancesVo>> tenantGroupMap =
        list.stream().collect(Collectors.groupingBy(vo -> vo.getTenantId()));

    for (val tenantId : tenantGroupMap.keySet()) {
      if (tenantId == 0) {
        continue;
      }

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
    if (self == null) {
      initialize();
    }
    self.get(tenantId);
  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-03-23
   * @updatedAt : 2022-03-23
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */

  @Cacheable(key = "{ #tenantId:#businessGroupType }",
      value = "ServiceInstancesResources::getListByTenantId")
  public List<ServiceInstancesVo> getListByTenantId(Integer tenantId, String businessGroupType)
      throws JsonMappingException, JsonProcessingException {
    val baseList = commonMapper.selectServiceInstancesBaseData(0, businessGroupType);
    val tenantlist = commonMapper.selectServiceInstancesBaseData(tenantId, businessGroupType);

    setJsonMap(baseList);
    setJsonMap(tenantlist);
    val baseGroupMap =
        baseList.stream().collect(Collectors.groupingBy(ServiceInstancesVo::getBusinessGroupType,
            Collectors.groupingBy(ServiceInstancesVo::getSourceKey)));
    val tenantGroupMap =
        tenantlist.stream().collect(Collectors.groupingBy(ServiceInstancesVo::getBusinessGroupType,
            Collectors.groupingBy(ServiceInstancesVo::getSourceKey)));
    val resultList = new ArrayList<ServiceInstancesVo>();


    for (val businessGroupTypeKey : baseGroupMap.keySet()) {
      val baseSourceGroup = baseGroupMap.get(businessGroupTypeKey);
      for (val baseSourceKey : baseSourceGroup.keySet()) {
        val voList = baseSourceGroup.get(baseSourceKey);
        val addMap = new HashMap<String, Object>();
        for (val vo : voList) {
          addMap.putAll(vo.getInherentMap());
        }

        val tenantSourceMap = tenantGroupMap.get(businessGroupTypeKey);
        if (tenantSourceMap != null) {
          val tenantVoList = tenantSourceMap.get(baseSourceKey);
          if (tenantVoList != null && tenantVoList.size() != 0) {
            for (val vo : tenantVoList) {
              addMap.putAll(vo.getInherentMap());
            }
          }
        }
        voList.get(0).setInherentMap(addMap);
        voList.get(0).setInherentJson(new JSONObject(addMap).toString());
        resultList.add(voList.get(0));

      }
    }



    return resultList;
  }

  @SuppressWarnings("unchecked")
  private void setJsonMap(List<ServiceInstancesVo> list)
      throws JsonMappingException, JsonProcessingException {
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
  public Map<String, List<ServiceInstancesVo>> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    if (self == null) {
      initialize();
    }
    return self.getListByTenantId(tenantId, this.businessGroupType).stream()
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
  public List<ServiceInstancesVo> get(Integer tenantId, String sourceKey)
      throws JsonMappingException, JsonProcessingException {
    if (self == null) {
      initialize();
    }
    return self.get(tenantId) != null ? self.get(tenantId).get(sourceKey) : null;
  }

  /**
   * スキマーキー取得
   *
   * @author : [AOT] s.paku
   * @createdAt : 2022/08/22
   * @updatedAt : 2022/08/22
   * @param tenantId
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getSchemaKeys")
  public Set<String> getSchemaKeys(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    if (self == null) {
      initialize();
    }
    val siList = self.get(tenantId) != null ? self.get(tenantId).get("ui_template") : null;
    if (siList == null || siList.size() < 1) {
      return null;
    }
    JSONObject jsonObject = new JSONObject(siList.get(0).getInherentJson());

    return jsonObject.keySet();
  }

  /**
   * 情報取得
   *
   * @author : [AOT] s.paku
   * @createdAt : 2022-06-09
   * @updatedAt : 2022-06-09
   * @param tenantId
   * @param sourceKey
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public List<ServiceInstancesVo> getLikeSorceKey(Integer tenantId, String sourceKey)
      throws JsonMappingException, JsonProcessingException {
    if (self == null) {
      initialize();
    }
    val map = self.get(tenantId);
    val result = new ArrayList<ServiceInstancesVo>();
    if (map != null) {
      val items = Maps.filterKeys(map, new Predicate<String>() {
        @Override
        public boolean apply(String key) {
          if (key.startsWith(sourceKey)) {
            return true;
          }
          return false;
        }
      });

      if (items != null && items.size() > 0) {
        items.values().forEach((value) -> {
          result.addAll(value);
        });
      } else {
        return null;
      }


    } else {
      return null;
    }

    return result;
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
    if (self == null) {
      initialize();
    }
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
    return list.stream().map(vo -> vo.getTenantId()).collect(Collectors.toSet());

  }

  /**
   * 最新更新日取得
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param tenantId
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Date getUpdatedAt(Integer tenantId) throws JsonMappingException, JsonProcessingException {

    if (self == null) {
      initialize();
    }

    val list = self.getListByTenantId(tenantId, this.businessGroupType);

    // last updatedAt
    return list.stream()
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

    if (self == null) {
      initialize();
    }

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
