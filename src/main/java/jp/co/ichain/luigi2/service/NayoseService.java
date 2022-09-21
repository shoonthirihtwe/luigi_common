package jp.co.ichain.luigi2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.ichain.luigi2.mapper.UserMapper;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeCommon.SexCode;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeCustomers.CorporateIndividualFlag;
import jp.co.ichain.luigi2.vo.NayoseResultVo;
import lombok.val;

/**
 * 名寄せサービス
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-04
 * @updatedAt : 2021-08-04
 */
@Service
public class NayoseService {

  @Autowired
  UserMapper userMapper;

  /**
   * 名寄せを行う
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-04
   * @updatedAt : 2021-08-04
   * @param tenantId
   * @param nameKanaSei
   * @param nameKanaMei
   * @param dateOfBirth
   * @param sex
   * @param corporateIndividualFlag
   * @param postalCode
   */
  @Transactional(transactionManager = "luigi2TransactionManager", readOnly = true)
  public NayoseResultVo nayose(Map<String, Object> param) {

    if (param.get("corporateIndividualFlag") == null && param.get("sex") != null) {
      if (param.get("sex").equals(SexCode.CORPORATE.toString())) {
        param.put("corporateIndividualFlag", CorporateIndividualFlag.CORPORATION.toString());
      } else {
        param.put("corporateIndividualFlag", CorporateIndividualFlag.INDIVIDUAL.toString());
      }
    }
    var resultList = userMapper.selectNayoseCustomerMatch(param);
    if (resultList != null && resultList.size() != 0) {
      List<String> idList = new ArrayList<String>();
      for (val result : resultList) {
        idList.add(result.getCustomerId());
      }
      resultList.get(0).setMatchedList(idList);
      return resultList.get(0);
    }

    if (resultList != null && resultList.size() == 0 && param.get("corporateIndividualFlag")
        .equals(CorporateIndividualFlag.INDIVIDUAL.toString())) {
      resultList = userMapper.selectNayoseCustomerIndividualPartialMatch(param);
    }

    if (resultList != null && resultList.size() != 0) {
      List<String> idList = new ArrayList<String>();
      for (val result : resultList) {
        idList.add(result.getCustomerId());
      }
      resultList.get(0).setPartialMatchedList(idList);
      return resultList.get(0);
    }
    return null;
  }
}
