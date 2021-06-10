package jp.co.ichain.luigi2.condition;

import java.util.List;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebConditionException;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import jp.co.ichain.luigi2.validity.Condition;

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
  public boolean overMax(Object data, List<Object> max) {
    if (max == null || max.size() < 1) {
      throw new WebConditionException(Luigi2Code.V0006);
    }

    return ((Integer) data) <= ((Double) max.get(0)).intValue();
  }
}
