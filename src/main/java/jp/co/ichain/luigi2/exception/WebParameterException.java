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

  public List<String> items = null;

  public WebParameterException(String code, List<String> args) {
    super(code, args);
  }

  public WebParameterException(String code, List<String> args, List<String> items) {
    super(code, args);
    this.items = items;
  }

  public WebParameterException(String... code) {
    super(code);
  }
}
