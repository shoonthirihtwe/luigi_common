package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
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

  private HashMap<Integer, Map<String, Map<String, Object>>> map = null;
  private Map<Integer, Date> updatedAtMap = null;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

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
  @PostConstruct
  public void initialize() throws JsonMappingException, JsonProcessingException {
    this.map = new HashMap<Integer, Map<String, Map<String, Object>>>();
    this.updatedAtMap = new HashMap<Integer, Date>();

    val tenantIdList = serviceInstancesResources.getTenantList();

    for (val tenantId : tenantIdList) {
      this.initialize(tenantId);
    }
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
  public void initialize(Integer tenantId) throws JsonMappingException, JsonProcessingException {
    var mapByTenant = serviceInstancesResources.get(tenantId);
    var list = mapByTenant.entrySet().stream().filter(entry -> entry.getKey().contains("_front"))
        .map(entry -> entry.getValue().get(0)).collect(Collectors.toList());

    // last updatedAt
    Date maxValue =
        list.stream().map(vo -> vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt())
            .max(Comparator.comparing(updatedAt -> updatedAt.getTime()))
            .orElseThrow(() -> new WebDataException(Luigi2ErrorCode.D0002));
    updatedAtMap.put(tenantId, maxValue);

    map.put(tenantId, list.stream().collect(
        Collectors.toMap(ServiceInstancesVo::getSourceKey, ServiceInstancesVo::getInherentMap)));
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
    if (this.map == null) {
      this.initialize();
    }

    return (updatedAt != null && updatedAt <= this.updatedAtMap.get(tenantId).getTime()) ? null
        : this.map.get(tenantId);

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
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId);
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
    if (this.map == null) {
      this.initialize();
    }

    return new ArrayList<Map<String, Map<String, Object>>>(this.map.values());
  }

}