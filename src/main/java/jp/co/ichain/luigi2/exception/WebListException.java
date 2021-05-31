package jp.co.ichain.luigi2.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * 例外リスト
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@SuppressWarnings("serial")
@Getter
public class WebListException extends RuntimeException {

  List<WebException> webExceptionList = null;

  public WebListException() {
    webExceptionList = new ArrayList<WebException>();
  }

  public void addWebException(WebException e) {
    this.webExceptionList.add(e);
  }
}
