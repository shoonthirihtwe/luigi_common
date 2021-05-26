package jp.co.ichain.luigi2.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * loginで発生するエラー
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class WebLoginException extends WebException {

  public WebLoginException(String... code) {
    super(code);
  }

  public WebLoginException(String code, Throwable cause) {
    super(code, cause);
  }

  public WebLoginException(String code, List<String> errArgs) {
    super(code, errArgs);
  }
}
