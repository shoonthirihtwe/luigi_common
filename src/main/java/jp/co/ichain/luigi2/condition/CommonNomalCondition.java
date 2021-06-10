package jp.co.ichain.luigi2.condition;

import java.util.List;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.validity.Condition;

/**
 * 共通条件検証サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-10
 * @updatedAt : 2021-06-10
 */
@Service
public class CommonNomalCondition implements Condition {

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
    return ((Integer) data) <= (int) (max != null && max.size() > 0 ? max.get(0) : 0);
  }
}
