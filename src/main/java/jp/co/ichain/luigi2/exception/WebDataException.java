package jp.co.ichain.luigi2.exception;

import java.util.List;

/**
 * データ更新エラー
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@SuppressWarnings("serial")
public class WebDataException extends WebException {

  public WebDataException(String code) {
    super(code);
  }

  public WebDataException(String code, List<? extends Object> args) {
    super(code, args);
  }

  public WebDataException(String code, Object... args) {
    super(code, args);
  }
}
