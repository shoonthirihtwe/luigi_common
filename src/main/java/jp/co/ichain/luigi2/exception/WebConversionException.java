package jp.co.ichain.luigi2.exception;

import java.util.List;

/**
 * 変換時例外
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
@SuppressWarnings("serial")
public class WebConversionException extends WebException {

  public WebConversionException(String code) {
    super(code);
  }

  public WebConversionException(String code, Object... args) {
    super(code, args);
  }

  public WebConversionException(String code, List<? extends Object> args) {
    super(code, args);
  }

}
