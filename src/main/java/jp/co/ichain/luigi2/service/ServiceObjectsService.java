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
import jp.co.ichain.luigi2.resources.code.ServiceObjectsCudCode;
import lombok.val;

@Service
public class ServiceObjectsService {

  @Autowired
  ServiceObjectsMapper mapper;

  @Autowired
  CommonContractMapper contractMapper;

  @SuppressWarnings("unchecked")
  @Transactional(transactionManager = "luigi2TransactionManager", rollbackFor = Exception.class)
  public void execute(Map<String, Object> paramMap, ServiceObjectsCudCode cudCode) {
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


    switch (cudCode) {
      case REGISTER:
        mapper.deleteAllSequenceNo(paramMap);
        mapper.insert(paramMap);
        break;
      default:
        val modifyMap = new HashMap<String, Object>();
        modifyMap.put("tenantId", paramMap.get("tenantId"));
        modifyMap.put("v", paramMap.get("v"));
        modifyMap.put("contractNo", paramMap.get("contractNo"));
        modifyMap.put("contractBranchNo", paramMap.get("contractBranchNo"));
        switch (cudCode) {
          case MODIFY:
            ((List<Map<String, Object>>) inherentList).forEach((map) -> {
              modifyMap.put("sequenceNo", map.get("sequenceNo"));
              modifyMap.put("data", map.get("data"));
              if (mapper.update(modifyMap) < 1) {
                mapper.insert(paramMap);
              }
            });
            break;
          case REMOVE:
            ((List<Map<String, Object>>) inherentList).forEach((map) -> {
              modifyMap.put("sequenceNo", map.get("sequenceNo"));
              mapper.delete(modifyMap);
            });
            break;
          default:
            return;
        }
        break;
    }
  }
}
