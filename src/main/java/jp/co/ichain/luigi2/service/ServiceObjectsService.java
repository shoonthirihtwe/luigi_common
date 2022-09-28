package jp.co.ichain.luigi2.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.mapper.CommonContractMapper;
import jp.co.ichain.luigi2.mapper.ServiceObjectsMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.resources.ServiceInstancesResources;
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
  ServiceInstancesResources serviceInstancesResources;

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

  /**
   * 固有データCRUD
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param paramMap
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
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

    val tenantId = (Integer) paramMap.get("tenantId");
    serviceInstancesResources.resetCacheableToUpdatedAt(tenantId);

    validateInherentList(tenantId, (List<? extends Object>) inherentList);
    // map to json
    Gson gson = new Gson();
    for (val map : ((List<Map<String, Object>>) inherentList)) {
      if (map.get("inherent") != null) {
        val inherent = map.get("inherent");
        if (inherent instanceof String) {
          map.put("data", inherent);
        } else {
          map.put("data", gson.toJson(inherent));
        }
      }
    }

    val modifyMap = new HashMap<String, Object>();
    modifyMap.put("tenantId", tenantId);
    modifyMap.put("v", paramMap.get("v"));
    modifyMap.put("contractNo", paramMap.get("contractNo"));
    modifyMap.put("contractBranchNo", paramMap.get("contractBranchNo"));

    for (val map : ((List<Map<String, Object>>) inherentList)) {
      val txType = map.get("txType");
      val data = mergeDatas(tenantId, (String) map.get("data"));
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
   * 外部固有検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @param inherentList
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws WebParameterException
   */
  @SuppressWarnings("unchecked")
  public void validateInherentList(Integer tenantId, List<? extends Object> inherentList)
      throws JsonMappingException, JsonProcessingException, WebParameterException {
    if (inherentList != null) {
      serviceInstancesResources.resetCacheableToUpdatedAt(tenantId);
      
      val schemaMap = serviceInstancesResources.getSchema(tenantId);
      if (schemaMap == null) {
        return;
      }

      // validate
      val exList = new ArrayList<WebException>();
      Gson gson = new Gson();
      for (val inherent : inherentList) {
        Map<String, Object> dataMap = null;
        if (inherent instanceof String) {
          dataMap = gson.fromJson((String) inherent, Map.class);
        } else {
          dataMap = (Map<String, Object>) inherent;
        }
        // 検証
        val inherentMap = dataMap.get("inherent");
        if (inherentMap != null) {
          Map<String, Object> inherentDataMap = null;
          if (inherentMap instanceof String) {
            inherentDataMap = gson.fromJson((String) inherentMap, Map.class);
          } else {
            inherentDataMap = (Map<String, Object>) inherentMap;
          }
          try {
            validate(tenantId, inherentDataMap, schemaMap);
          } catch (WebException e) {
            exList.add(e);
          }
        }

      }

      if (exList.size() > 0) {
        throw new WebParameterException(Luigi2ErrorCode.V0000, exList);
      }
    }
  }

  /**
   * 固有データ検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @param dataMap
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws WebParameterException
   */
  @SuppressWarnings("unchecked")
  private void validate(Integer tenantId, Map<String, Object> dataMap,
      Map<String, Object> schemaMap)
      throws JsonMappingException, JsonProcessingException, WebParameterException {
    if (dataMap == null || schemaMap == null) {
      return;
    }

    // validity
    for (String key : dataMap.keySet()) {
      if (schemaMap.get(key) == null) {
        throw new WebDataException(Luigi2ErrorCode.D0002, key, dataMap.get(key));
      }
    }

    for (String key : schemaMap.keySet()) {
      validate(tenantId, key, dataMap.get(key), (Map<String, Object>) schemaMap.get(key));
    }
  }

  /**
   * 固有データ検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param key
   * @param data
   * @param schemaInfoMap
   * @throws WebParameterException
   */
  private void validate(Integer tenantId, String key, Object data,
      Map<String, Object> schemaInfoMap) throws WebParameterException {

    // required
    val requiredValidity = schemaInfoMap.get("required");
    if (requiredValidity != null && (boolean) requiredValidity) {
      if (data == null) {
        throw new WebParameterException(Luigi2ErrorCode.V0001, key);
      }
    }

    // type
    val typeValidity = schemaInfoMap.get("type");
    if (typeValidity != null && data != null) {

      val lengthValidity = schemaInfoMap.get("length");
      switch ((String) typeValidity) {
        case "uint":
          Long ndata;
          if (data instanceof Integer) {
            ndata = ((Integer) data).longValue();
          } else if (data instanceof Long) {
            ndata = (long) data;
          } else if (data instanceof Double) {
            if (((Double) data) % 1 > 0) {
              throw new WebParameterException(Luigi2ErrorCode.V0005, key);
            }
            ndata = ((Double) data).longValue();
          } else {
            throw new WebParameterException(Luigi2ErrorCode.V0005, key);
          }

          if (ndata < 0) {
            throw new WebParameterException(Luigi2ErrorCode.V0002, key, 0);
          }
          if (lengthValidity != null) {
            int length = (int) (Math.log10(ndata) + 1);
            if (length > (int) lengthValidity) {
              throw new WebParameterException(Luigi2ErrorCode.V0003, key, lengthValidity);
            }
          }
          break;

        case "date":
          if (data instanceof String) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
              sdf.parse((String) data);
            } catch (ParseException e) {
              throw new WebParameterException(Luigi2ErrorCode.V0005, key);
            }
          } else if ((data instanceof Date || data instanceof Long
              || data instanceof Double) == false) {
            throw new WebParameterException(Luigi2ErrorCode.V0005, key);
          }

          break;
        case "enum":
          val enumList = serviceInstancesResources.getEnumValues(tenantId, key);
          var enumData = data;
          if (data instanceof Double) {
            enumData = ((Double) data).longValue();
          } else {
            enumData = data;
          }
          if (enumList.contains(String.valueOf(enumData)) == false) {
            throw new WebParameterException(Luigi2ErrorCode.V0004, key);
          }
          break;

        case "string":
          if (data instanceof String == false) {
            throw new WebParameterException(Luigi2ErrorCode.V0005, key);
          }
          if (lengthValidity != null) {
            if (((String) data).length() > (int) lengthValidity) {
              throw new WebParameterException(Luigi2ErrorCode.V0003, key, lengthValidity);
            }
          }
          break;
        default:
          break;
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

    val schemaKeys = serviceInstancesResources.getSchemaKeys(tenantId);
    for (val key : CollectionUtils.safe(schemaKeys)) {
      if (jsonObject.isNull(key)) {
        jsonObject.put(key, "");
      }
    }

    return jsonObject.toString();
  }
}
