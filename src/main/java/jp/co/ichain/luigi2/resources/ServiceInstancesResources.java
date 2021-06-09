package jp.co.ichain.luigi2.resources;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.util.StringUtils;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
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
public class ServiceInstancesResources {

  private Map<Integer, Map<String, List<ServiceInstancesVo>>> map = null;
  private Map<Integer, Date> updatedAtMap = null;

  @Autowired
  CommonMapper commonMapper;

  /**
   * 初期化する
   *
   * @author : [AOT] s.paku
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   */
  @SuppressWarnings("unchecked")
  @Lock(LockType.WRITE)
  @PostConstruct
  public void initialize() throws JsonMappingException, JsonProcessingException {
    this.map = new HashMap<Integer, Map<String, List<ServiceInstancesVo>>>();
    this.updatedAtMap = new HashMap<Integer, Date>();

    val list = commonMapper.selectServiceInstances(null);
    Map<Integer, List<ServiceInstancesVo>> tenantGroupMap =
        list.stream().collect(Collectors.groupingBy(vo -> vo.getTenantId()));

    for (val tenantId : tenantGroupMap.keySet()) {
      val listByTenant = tenantGroupMap.get(tenantId);

      // json map setting
      for (val vo : listByTenant) {
        if (StringUtils.isNullOrEmpty(vo.getInherentJson()) == false) {
          ObjectMapper mapper = new ObjectMapper();
          vo.setInherentMap(mapper.readValue(vo.getInherentJson(), Map.class));
        }
      }

      // last updatedAt
      Date maxValue = listByTenant.stream()
          .map(vo -> vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt())
          .max(Comparator.comparing(updatedAt -> updatedAt.getTime()))
          .orElseThrow(() -> new WebDataException(Luigi2Code.D0002));
      updatedAtMap.put(tenantId, maxValue);

      // sourceKey Group
      this.map.put(tenantId,
          listByTenant.stream().collect(Collectors.groupingBy(vo -> vo.getSourceKey())));;
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
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId);
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
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId).get(sourceKey);
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
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId).get(sourceKey).stream().findFirst();
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
  public Set<Integer> getTenantList() throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return this.map.keySet();
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
  public Date getUpdatedAt(Integer tenantId) {
    return updatedAtMap.get(tenantId);
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
    if (this.map == null) {
      this.initialize();
    }

    Date lastUpdatedAt = updatedAtMap.get(tenantId);
    val mapByTenant = this.get(tenantId);

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
      throw new WebException(Luigi2Code.S0000);
    }
  }
}