package jp.co.ichain.luigi2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import jp.co.ichain.luigi2.mapper.CommonContractMapper;
import jp.co.ichain.luigi2.mapper.ServiceObjectsMapper;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.util.CollectionUtils;
import jp.co.ichain.luigi2.vo.ServiceObjectsVo;
import lombok.val;

@Service
public class ServiceObjectsService {

  @Autowired
  ServiceObjectsMapper mapper;

  @Autowired
  CommonContractMapper contractMapper;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  /**
   * 既存データ全件削除
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-08-03
   * @updatedAt : 2022-08-03
   * @param paramMap
   */
  public void removeAllSequenceNo(Map<String, Object> paramMap) {
    if (paramMap.get("v") == null) {
      paramMap.put("v", mapper.selectMaxVersion(paramMap));
    }

    mapper.deleteAllSequenceNo(paramMap);
  }

  /**
   * 固有データ取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/06
   * @updatedAt : 2022/09/06
   * @param param
   * @return
   */
  public List<ServiceObjectsVo> getServiceObjects(Map<String, Object> param) {
    return mapper.selectServiceObjects(param);
  }

  @SuppressWarnings("unchecked")
  @Transactional(transactionManager = "luigi2TransactionManager", rollbackFor = Exception.class)
  public void execute(Map<String, Object> paramMap)
      throws JsonMappingException, JsonProcessingException {
    var inherentList = paramMap.get("inherentList");

    // inherentList null check
    if (inherentList == null) {
      return;
    }

    if (paramMap.get("contractBranchNo") == null) {
      paramMap.put("contractBranchNo", contractMapper.selectMaxContractBranchNo(paramMap));
    }

    if (paramMap.get("v") == null) {
      paramMap.put("v", mapper.selectMaxVersion(paramMap));
    }

    Gson gson = new Gson();
    // map to json
    ((List<Map<String, Object>>) inherentList).forEach((map) -> {
      if (map.get("inherent") != null) {
        val inherent = map.get("inherent");
        if (inherent instanceof String) {
          map.put("data", inherent);
        } else {
          map.put("data", gson.toJson(map.get("inherent")));
        }
      }
    });

    val modifyMap = new HashMap<String, Object>();
    modifyMap.put("tenantId", paramMap.get("tenantId"));
    modifyMap.put("v", paramMap.get("v"));
    modifyMap.put("contractNo", paramMap.get("contractNo"));
    modifyMap.put("contractBranchNo", paramMap.get("contractBranchNo"));

    for (val map : ((List<Map<String, Object>>) inherentList)) {
      val txType = map.get("txType");
      val data = mergeDatas((Integer) paramMap.get("tenantId"), (String) map.get("data"));
      modifyMap.put("data", data);
      modifyMap.put("sequenceNo", map.get("sequenceNo"));

      if ("C".equals(txType)) {
        mapper.insert(modifyMap);
      } else if ("D".equals(txType)) {
        mapper.delete(modifyMap);
      } else {
        if (mapper.update(modifyMap) < 1) {
          mapper.insert(modifyMap);
        }
      }
    }
  }

  /**
   * Jsonに含まれてないスキマーを搭載
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/08/22
   * @updatedAt : 2022/08/22
   * @param tenantId
   * @param jsonData
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  private String mergeDatas(Integer tenantId, String jsonData)
      throws JsonMappingException, JsonProcessingException {
    JSONObject jsonObject = new JSONObject(jsonData);

    val schemaKeys = serviceInstancesBaseResources.getSchemaKeys(tenantId);
    for (val key : CollectionUtils.safe(schemaKeys)) {
      if (jsonObject.isNull(key)) {
        jsonObject.put(key, "");
      }
    }

    return jsonObject.toString();
  }
}
