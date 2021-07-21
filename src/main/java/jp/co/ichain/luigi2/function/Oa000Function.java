package jp.co.ichain.luigi2.function;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.Luigi2DateCode;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
import jp.co.ichain.luigi2.si.function.Function;
import lombok.val;

/**
 * 共通関数サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-16
 * @updatedAt : 2021-07-16
 */
@Service
@Function
public class Oa000Function {

  @Autowired
  CommonMapper mapper;

  /**
   * テスト用
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param data
   * @param max
   * @return
   */
  public Object getOa001(Integer tenantId, Object... params) {
    if (params == null || params.length < 4) {
      throw new WebParameterException(Luigi2ErrorCode.V0000);
    }
    val paramMap = new HashMap<String, Object>();
    paramMap.put("tenantId", tenantId);
    paramMap.put("salesPlanCode", params[0]);
    paramMap.put("salesPlanTypeCode", params[1]);
    paramMap.put("issueDate", params[4]);

    val dateCode = (String) params[2];
    val standardDateCal = Calendar.getInstance();
    standardDateCal.setTime((Date) params[3]);

    switch (dateCode) {
      // 解約消滅日
      case Luigi2DateCode.C00001:
        val salesPlanVo = mapper.selectSalesProducts(paramMap);
        switch (salesPlanVo.getTerminationDatePattern()) {
          case "MB":
            standardDateCal.set(Calendar.DAY_OF_MONTH, 1);
            return standardDateCal.getTime();
          case "ME":
            standardDateCal.set(Calendar.DAY_OF_MONTH,
                standardDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            return standardDateCal.getTime();
          case "MO":
            return standardDateCal.getTime();
          default:
            throw new WebDataException(Luigi2ErrorCode.D0002, "terminationDatePattern");
        }
      default:
        throw new WebDataException(Luigi2ErrorCode.D0002, "dataCode");
    }
  }

  /**
   * 日付が現時刻以上
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param data
   * @param max
   * @return
   */
  public boolean overCurrentDate(Object data, Integer tenantId, List<Object> paramList) {
    return ((long) data) <= new Date().getTime();
  }

  /**
   * キーに値するデータ存在チェック
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param data
   * @param max
   * @return
   */
  public boolean checkExistKey(Object data, Integer tenantId, List<Object> paramList) {
    val key = (String) paramList.get(0);
    Boolean result = mapper.selectIsExistKey(Luigi2TableInfo.getLockTable(TableInfo.valueOf(key)),
        tenantId, data);
    return result != null ? result : false;
  }
}
