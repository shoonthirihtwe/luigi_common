package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.vo.ValidityVo;
import lombok.val;

/**
 * 検証リソース
 *
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-07
 * @updatedAt : 2021-05-07
 */
@Singleton
@Service
public class ValidityResources {

  private ValidityResources self;
  private final ApplicationContext applicationContext;
  private final ServiceInstancesBaseResources serviceInstancesBaseResources;

  @Autowired
  TenantResources tenantResources;

  ValidityResources(ApplicationContext applicationContext,
      ServiceInstancesBaseResources serviceInstancesBaseResources) {
    this.applicationContext = applicationContext;
    this.serviceInstancesBaseResources = serviceInstancesBaseResources;
  }

  /**
   * 検証を初期化する
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

    self = applicationContext.getBean(ValidityResources.class);
    val tenantList = tenantResources.getAll();

    for (val tenant : tenantList) {
      self.initialize(tenant.getId());
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
    return serviceInstancesBaseResources.get(tenantId).entrySet().stream()
        .filter(entry -> entry.getKey().equals("validity")).map(entry -> {
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
  public Map<String, ValidityVo> get(Integer tenantId, Long updatedAt)
      throws JsonMappingException, JsonProcessingException {

    return (updatedAt != null && updatedAt <= self.getLastUpdatedAt(tenantId).getTime() ? null
        : self.get(tenantId));
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
  public Map<String, ValidityVo> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {

    var mapByTenant = serviceInstancesBaseResources.get(tenantId);
    var list = mapByTenant.get("validity");

    Gson gson = new Gson();
    val validityMap = new HashMap<String, ValidityVo>();
    for (val vo : list) {
      JSONObject jsonObject = new JSONObject(vo.getInherentJson());

      for (val key : jsonObject.keySet()) {
        val tableJsonObject = jsonObject.getJSONObject(key);
        if (tableJsonObject != null) {
          validityMap.put(key, gson.fromJson(tableJsonObject.toString(), ValidityVo.class));
        }
      }
    }

    return validityMap;
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
  public List<Map<String, ValidityVo>> getAll()
      throws JsonMappingException, JsonProcessingException {

    val tenantList = tenantResources.getAll();

    val resultList = new ArrayList<Map<String, ValidityVo>>();
    for (val tenant : tenantList) {
      resultList.add(self.get(tenant.getId()));
    }

    return resultList;
  }

}
