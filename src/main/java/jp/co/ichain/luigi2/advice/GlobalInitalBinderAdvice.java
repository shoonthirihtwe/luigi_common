package jp.co.ichain.luigi2.advice;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 起動時初期処理、Binder時処理
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-27
 * @updatedAt : 2021-05-27
 */
@ControllerAdvice
public class GlobalInitalBinderAdvice {

  @Value("${config.time-zone}")
  private String timeZone;

  /**
   * パラメーターをバインディングする時の前処理 ・Dateタイプの変換 ・フィールドに設定されている有効性検証を実行
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param binder
   * @param request
   */
  @InitBinder
  public void initBinder(WebDataBinder binder, HttpServletRequest request) {
    binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String value) {
        setValue(new Date(Long.valueOf(value)));
      }
    });
  }

  /**
   * 起動時処理
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   */
  @PostConstruct
  void initialize() {
    TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
  }
}
