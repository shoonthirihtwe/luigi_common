package jp.co.ichain.luigi2.convert;

import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.validity.Conversion;

/**
 * 共通変換サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
@Service
@Conversion
public class CommonContractConvert {


  /**
   * ハイフン排除
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @param data
   * @param tenantId
   * @return
   */
  public String removeHyphen(Object data, Integer tenantId) {
    return ((String) data).replace("-", "");
  }
}
