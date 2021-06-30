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
public class WebAwsException extends WebException {

  public WebAwsException(String code) {
    super(code);
  }

  public WebAwsException(String code, Object... args) {
    super(code, args);
  }

  public WebAwsException(String code, List<? extends Object> args) {
    super(code, args);
  }

}
