package jp.co.ichain.luigi2.advice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import jp.co.ichain.luigi2.dto.ResultListDto;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebListException;
import jp.co.ichain.luigi2.resources.ErrorResources;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ＠RestControllerが付与されいるController内でException処理
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-27
 * @updatedAt : 2021-05-27
 */
@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class WebControllerErrorAdvice {

  @Value("${spring.servlet.multipart.max-file-size}")
  private String maxFileSize;

  @Autowired
  private ErrorResources errorResources;

  String SYSTEM_ERROR_DESC = "SYSTEM ERROR";

  @PostConstruct
  void initialize() throws IOException {
    SYSTEM_ERROR_DESC = errorResources.get(Luigi2Code.S000_S0002);
  }

  /**
   * 予想外システムエラー処理
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param e
   * @return
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Produces(MediaType.APPLICATION_JSON)
  @ExceptionHandler(Exception.class)
  public @ResponseBody ResultListDto<String> handleException(Exception e) {
    val result = new ResultListDto<String>();

    try {
      e.printStackTrace();
      result.setCode(Luigi2Code.S000_S0002);
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      ex.printStackTrace();
      result.setItems(Arrays.asList(SYSTEM_ERROR_DESC));
    }

    return result;
  }

  /**
   * APIエラー発生
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param e
   * @return
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Produces(MediaType.APPLICATION_JSON)
  @ExceptionHandler(WebException.class)
  public @ResponseBody ResultListDto<String> handleWebException(WebException e) {
    val result = new ResultListDto<String>();

    try {
      String[] code = e.getCode().split("_");
      result.setCode(code[1]);
      result.setItems(Arrays.asList(makeDescForWebException(code[0], e.getErrArgs())));
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      result.setCode(Luigi2Code.S000_S0002);
      result.setItems(Arrays.asList(SYSTEM_ERROR_DESC));
    }

    return result;
  }

  /**
   * APIエラー発生（リスト）
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param e
   * @return
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Produces(MediaType.APPLICATION_JSON)
  @ExceptionHandler(WebListException.class)
  public @ResponseBody ResultListDto<String> handleWebException(WebListException eList) {
    val result = new ResultListDto<String>();

    try {
      result.setCode("V0001");
      val descList = new ArrayList<String>();

      for (val e : eList.getWebExceptionList()) {
        String[] code = e.getCode().split("_");
        descList.add(makeDescForWebException(code[0], e.getErrArgs()));
      }
      result.setItems(descList);
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      result.setCode(Luigi2Code.S000_S0002);
      result.setItems(Arrays.asList(SYSTEM_ERROR_DESC));
    }

    return result;
  }

  /**
   * ファイルアップロード時エラー発生
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param e
   * @return
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Produces(MediaType.APPLICATION_JSON)
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public @ResponseBody ResultListDto<String> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException e) {
    val result = new ResultListDto<String>();

    try {
      val rootEx = e.getRootCause();
      if (rootEx instanceof FileSizeLimitExceededException) {
        result.setCode(Luigi2Code.S000_S0002);
      } else if (rootEx instanceof SizeLimitExceededException) {
        result.setCode(Luigi2Code.S000_S0002);
      } else {
        result.setCode(Luigi2Code.S000_S0002);
      }
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      result.setCode(Luigi2Code.S000_S0002);
      result.setItems(Arrays.asList(SYSTEM_ERROR_DESC));
    }

    return result;
  }

  /**
   * エラー説明リスト作成
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param e
   * @return
   * @throws IOException
   */
  private String makeDescForWebException(String code, List<String> errArgs) throws IOException {
    String desc = errorResources.get(code);
    if (desc != null) {
      if (errArgs != null && errArgs.size() > 0) {
        desc = String.format(desc, errArgs);
      }
    }
    return desc;
  }
}
