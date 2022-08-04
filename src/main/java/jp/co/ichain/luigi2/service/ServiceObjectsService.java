package jp.co.ichain.luigi2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import jp.co.ichain.luigi2.mapper.CommonContractMapper;
import jp.co.ichain.luigi2.mapper.ServiceObjectsMapper;
import lombok.val;

@Service
public class ServiceObjectsService {

  @Autowired
  ServiceObjectsMapper mapper;

  @Autowired
  CommonContractMapper contractMapper;

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

  @SuppressWarnings("unchecked")
  @Transactional(transactionManager = "luigi2TransactionManager", rollbackFor = Exception.class)
  public void execute(Map<String, Object> paramMap) {
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
        map.put("data", gson.toJson(map.get("inherent")));
      }
    });

    val modifyMap = new HashMap<String, Object>();
    modifyMap.put("tenantId", paramMap.get("tenantId"));
    modifyMap.put("v", paramMap.get("v"));
    modifyMap.put("contractNo", paramMap.get("contractNo"));
    modifyMap.put("contractBranchNo", paramMap.get("contractBranchNo"));
    ((List<Map<String, Object>>) inherentList).forEach((map) -> {
      val status = map.get("status");
      modifyMap.put("data", map.get("data"));
      modifyMap.put("sequenceNo", map.get("sequenceNo"));

      if ("C".equals(status)) {
        mapper.insert(modifyMap);
      } else if ("D".equals(map.get("status"))) {
        mapper.delete(modifyMap);
      } else {
        if (mapper.update(modifyMap) < 1) {
          mapper.insert(modifyMap);
        }
      }
    });
  }
}
