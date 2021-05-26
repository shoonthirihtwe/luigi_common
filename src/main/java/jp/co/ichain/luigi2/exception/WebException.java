package jp.co.ichain.luigi2.exception;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Webで発生するエラー
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class WebException extends RuntimeException {
  /** バグコード */
  String code;
  /** エラー引数 */
  List<String> errArgs;
  /** 原因となった例外 */
  Throwable cause;

  public WebException(String... code) {
    this.code = code[0];
    if (code.length > 0) {
      this.errArgs = Arrays.asList(Arrays.copyOfRange(code, 1, code.length));
    }
  }

  public WebException(String code, Throwable cause) {
    this.code = code;
    this.cause = cause;
  }

  public WebException(String code, List<String> errArgs) {
    this.code = code;
    this.errArgs = errArgs;
  }
}
