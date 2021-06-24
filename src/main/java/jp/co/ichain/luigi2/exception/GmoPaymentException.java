package jp.co.ichain.luigi2.exception;

import java.util.Map;
import jp.co.ichain.luigi2.vo.GmoPaymentErrorVo;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;

/**
 * Gmo Exception
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
public class GmoPaymentException extends Exception {

  private static final long serialVersionUID = -1418179781799545264L;

  private Integer responseCode;
  private GmoPaymentVo gmoPaymentVo;

  public GmoPaymentException(Integer responseCode) {
    this.responseCode = responseCode;
  }

  public GmoPaymentException(GmoPaymentVo gmoPaymentVo) {
    this.gmoPaymentVo = gmoPaymentVo;
  }

  public Integer getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(Integer responseCode) {
    this.responseCode = responseCode;
  }

  public GmoPaymentVo getGmoPaymentVo() {
    return gmoPaymentVo;
  }

  @Override
  public String getMessage() {
    StringBuffer result = new StringBuffer();
    Map<String, GmoPaymentErrorVo> map = gmoPaymentVo.getErrorMap();
    if (gmoPaymentVo != null && map != null) {
      for (String key : map.keySet()) {
        GmoPaymentErrorVo errorVo = map.get(key);
        result.append(errorVo.getErrCode()).append(":").append(errorVo.getErrInfo()).append(", ");
      }
    } else {
      result.append("HTTP ERROR:").append(responseCode);
    }
    return new String(result);

  }
}
