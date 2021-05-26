package jp.co.ichain.luigi2.exception;

/**
 * データ更新エラー
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@SuppressWarnings("serial")
public class WebUpdatedException extends WebException {
  public Object result;

  public WebUpdatedException(String code) {
    super(code);
  }
}
