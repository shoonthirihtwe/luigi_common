package jp.co.ichain.luigi2.exception;

import java.util.Map;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;
import jp.co.ichain.luigi2.vo.PaymentErrorVo;

/**
 * Gmo Exception
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
public class GmoPaymentException extends Exception {

  private static final long serialVersionUID = -1418179781799545264L;

  private String responseMessage;
  private GmoPaymentVo gmoPaymentVo;

  public GmoPaymentException(Integer responseCode) {
    this.responseMessage = String.valueOf(responseCode);
  }

  public GmoPaymentException(GmoPaymentVo gmoPaymentVo) {
    this.gmoPaymentVo = gmoPaymentVo;
  }

  public GmoPaymentException(String message) {
    this.responseMessage = message;
  }

  public GmoPaymentVo getGmoPaymentVo() {
    return gmoPaymentVo;
  }

  @Override
  public String getMessage() {
    StringBuffer result = new StringBuffer();
    Map<String, PaymentErrorVo> map = gmoPaymentVo.getErrorMap();
    if (gmoPaymentVo != null && map != null) {
      for (String key : map.keySet()) {
        PaymentErrorVo errorVo = map.get(key);
        result.append(errorVo.getErrCode()).append(":").append(errorVo.getErrInfo()).append(", ");
      }
    } else {
      result.append("ERROR:").append(responseMessage);
    }
    return new String(result);

  }
}
