package jp.co.ichain.luigi2.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.ichain.luigi2.mapper.UserMapper;
import jp.co.ichain.luigi2.vo.NayoseRequestVo;
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
   * @param paramMap
   * @param endpoint
   */
  @Transactional(transactionManager = "luigi2TransactionManager", readOnly = true)
  public NayoseResultVo nayose(NayoseRequestVo param) {

    var resultList = userMapper.selectNayoseCustomerMatch(param);
    if (resultList != null) {
      List<String> idList = new ArrayList<String>();
      for (val result : resultList) {
        idList.add(result.getCustomerId());
      }
      resultList.get(0).setMatchedList(idList);
      return resultList.get(0);
    }

    if (resultList == null && !param.getSex().equals("3")) {
      resultList = userMapper.selectNayoseCustomerIndividualPartialMatch(param);
    }

    if (resultList != null) {
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
