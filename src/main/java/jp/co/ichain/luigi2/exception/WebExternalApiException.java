package jp.co.ichain.luigi2.exception;

import java.util.List;

/**
 * 条件検証時に発生する例外
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-10
 * @updatedAt : 2021-06-10
 */
@SuppressWarnings("serial")
public class WebExternalApiException extends WebException {

  public WebExternalApiException(String code) {
    super(code);
  }

  public WebExternalApiException(String code, Object... args) {
    super(code, args);
  }

  public WebExternalApiException(String code, List<? extends Object> args) {
    super(code, args);
  }

}
