package jp.co.ichain.luigi2.exception;

import java.util.List;

/**
 * パラメーター検証時に発生する例外
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@SuppressWarnings("serial")
public class WebParameterException extends WebException {

  public WebParameterException(String code) {
    super(code);
  }

  public WebParameterException(String code, Object... args) {
    super(code, args);
  }

  public WebParameterException(String code, List<? extends Object> args) {
    super(code, args);
  }

}
