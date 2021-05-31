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
public class WebUpdatedException extends WebException {

  public WebUpdatedException(String code, List<String> args) {
    super(code, args);
  }

  public WebUpdatedException(String... code) {
    super(code);
  }
}
