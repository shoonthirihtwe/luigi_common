package jp.co.ichain.luigi2.advice;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import jp.co.ichain.luigi2.dto.ResultListDto;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.vo.ErrorVo;
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
      result.setCode(Luigi2ErrorCode.S0000);
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      ex.printStackTrace();
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
  @SuppressWarnings("unchecked")
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Produces(MediaType.APPLICATION_JSON)
  @ExceptionHandler(WebException.class)
  public @ResponseBody ResultListDto<Object> handleWebException(WebException e) {
    val result = new ResultListDto<Object>();

    try {
      result.setCode(e.getCode());
      result.setItems((List<Object>) e.getErrArgs());
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      result.setCode(Luigi2ErrorCode.S0000);
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
  @ExceptionHandler(WebParameterException.class)
  public @ResponseBody ResultListDto<ErrorVo> handleWebException(WebParameterException e) {
    val result = new ResultListDto<ErrorVo>();

    try {
      result.setCode(e.getCode());
      if (e.getErrArgs() != null) {

        result.setItems(e.getErrArgs().stream()
            .map(
                ex -> new ErrorVo(((WebException) ex).getCode(), ((WebException) ex).getErrArgs(),
                    ((WebException) ex).getParentKey(), ((WebException) ex).getArrayIndex()))
            .collect(Collectors.toList()));
      }
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      result.setCode(Luigi2ErrorCode.S0000);
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
        result.setCode(Luigi2ErrorCode.S0000);
      } else if (rootEx instanceof SizeLimitExceededException) {
        result.setCode(Luigi2ErrorCode.S0000);
      } else {
        result.setCode(Luigi2ErrorCode.S0000);
      }
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      result.setCode(Luigi2ErrorCode.S0000);
    }

    return result;
  }
}
