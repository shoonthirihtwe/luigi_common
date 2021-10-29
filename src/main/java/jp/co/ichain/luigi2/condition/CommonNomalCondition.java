package jp.co.ichain.luigi2.condition;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
import jp.co.ichain.luigi2.resources.TenantResources;
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

  @Autowired
  TenantResources tenantResources;

  static final String ZIP_REX = "^[0-9]{7}$";

  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * 日付が現時刻以上
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param data
   * @param max
   * @return
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public boolean overCurrentDate(Object data, Integer tenantId, List<Object> paramList)
      throws InstantiationException, IllegalAccessException, SecurityException {
    Long time = null;
    try {
      time = sdf.parse(data.toString()).getTime();
    } catch (Exception e) {
      time = (Long) data;
    }
    return time >= tenantResources.get(tenantId).getOnlineDate().getTime();
  }

  /**
   * 日付が現時刻以下
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-19
   * @updatedAt : 2021-07-19
   * @param data
   * @param max
   * @return
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public boolean underCurrentDate(Object data, Integer tenantId, List<Object> paramList)
      throws InstantiationException, IllegalAccessException, SecurityException {
    Long time = null;
    try {
      time = sdf.parse(data.toString()).getTime();
    } catch (Exception e) {
      time = (Long) data;
    }
    return time <= tenantResources.get(tenantId).getOnlineDate().getTime();
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

  /**
   * 郵便番号チェック
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-19
   * @updatedAt : 2021-07-19
   * @param data
   * @return
   */
  public boolean checkZipcode(Object data, Integer tenantId, List<Object> paramList) {
    String zipcode = (String) data;
    if (zipcode.contains("-")) {
      zipcode = zipcode.replace("-", "");
    }
    return zipcode.matches(ZIP_REX);

  }
}
