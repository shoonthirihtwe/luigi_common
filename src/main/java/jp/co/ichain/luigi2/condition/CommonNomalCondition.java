package jp.co.ichain.luigi2.condition;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
import jp.co.ichain.luigi2.service.AuthService;
import jp.co.ichain.luigi2.validity.Condition;
import lombok.val;

/**
 * 共通条件検証サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-10
 * @updatedAt : 2021-06-10
 */
@Service
@Condition
public class CommonNomalCondition {

  @Autowired
  CommonMapper mapper;

  @Autowired
  AuthService authService;

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
  public boolean overMax(Object data, Integer tenantId, List<Object> max) {
    return ((Integer) data) <= ((Double) max.get(0)).intValue();
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
