package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
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

  private HashMap<Integer, Map<String, ValidityVo>> map = null;

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
    this.map = new HashMap<Integer, Map<String, ValidityVo>>();

    val tenantIdList = serviceInstancesResources.getTenantList();

    for (val tenantId : tenantIdList) {
      var mapByTenant = serviceInstancesResources.get(tenantId);
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
      map.put(tenantId, validityMap);
    }

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
  public List<Map<String, ValidityVo>> getAll()
      throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return new ArrayList<Map<String, ValidityVo>>(this.map.values());
  }

}
