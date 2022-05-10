package jp.co.ichain.luigi2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;
import jp.co.ichain.luigi2.vo.PaymentErrorVo;

/**
 * Gmo Api Utils
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
public class GmoPaymentApiUtils {

  /**
   * GMO API通信を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param strUrl
   * @param method
   * @param params
   * @param requestEncoding
   * @param responseEncoding
   * @return
   * @throws IOException
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws ParseException
   */
  public static synchronized GmoPaymentVo doHttpResult(String strUrl, String method, String params,
      String requestEncoding, String responseEncoding) throws IOException, GmoPaymentException,
      IllegalArgumentException, IllegalAccessException, ParseException {
    return doHttpResult(strUrl, method, params, requestEncoding, responseEncoding, 5000, 5000,
        null);
  }

  /**
   * GMO API通信を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param strUrl
   * @param method
   * @param params
   * @param requestEncoding
   * @param responseEncoding
   * @param connectionTime
   * @param timeout
   * @param responseContentType
   * @return
   * @throws IOException
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws ParseException
   */
  public static synchronized GmoPaymentVo doHttpResult(String strUrl, String method, String params,
      String requestEncoding, String responseEncoding, Integer connectionTime, Integer timeout,
      String responseContentType) throws IOException, GmoPaymentException, IllegalArgumentException,
      IllegalAccessException, ParseException {

    GmoPaymentVo result = new GmoPaymentVo();
    HttpURLConnection conn = null;
    /*
     * Request
     */
    if (method != null) {
      method = method.toUpperCase(Locale.getDefault());
    }

    try {
      if ("GET".equals(method)) {
        StringBuffer sb = new StringBuffer();
        sb.append(strUrl);
        if (params != null && params.equals("") == false) {
          sb.append("?").append(params);
        }
        strUrl = new String(sb);
      }
      URL url = new URL(strUrl);

      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod(method);

      if (timeout != null) {
        conn.setReadTimeout(timeout);
      }
      if (connectionTime != null) {
        conn.setConnectTimeout(connectionTime);
      }

      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("Cache-Control", "no-cache");

      conn.setInstanceFollowRedirects(true);
      HttpURLConnection.setFollowRedirects(true);

      conn.setDoInput(true);
      if ("POST".equals(method)) {
        conn.setDoOutput(true);
        OutputStream outstream = conn.getOutputStream();

        outstream.write(params.getBytes(requestEncoding));
        outstream.flush();
        outstream.close();
      }

      conn.connect();
      int httpRc = conn.getResponseCode();
      StringBuilder responseSb = new StringBuilder();
      if (httpRc == HttpURLConnection.HTTP_OK) {

        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "windows-31j");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
          responseSb.append(line);
          responseSb.append("\r\n");
        }

        // レスポンスのセット
        Map<String, String> responseParamMap = new HashMap<String, String>();
        String[] resParams = responseSb.toString().trim().split("&");
        for (int i = 0; i < resParams.length; i++) {
          String[] keyValue = resParams[i].split("=");
          keyValue[0] = keyValue[0].substring(0, 1).toLowerCase() + keyValue[0].substring(1);
          responseParamMap.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : null);
        }
        ClassUtils.setFieldValueAndConvert(responseParamMap, result);

        String errCode = responseParamMap.get("errCode");
        if (StringUtils.isEmpty(errCode) == false) {
          Map<String, PaymentErrorVo> errorMap = new HashMap<String, PaymentErrorVo>();
          String[] errorCodes = errCode.split("\\|");
          String[] errorInfos = responseParamMap.get("errInfo").split("\\|");
          for (int i = 0; i < errorInfos.length; i++) {
            errorMap.put(errorInfos[i], new PaymentErrorVo(errorCodes[i], errorInfos[i]));
          }
          result.setErrorMap(errorMap);
        }
      } else {
        throw new GmoPaymentException(httpRc);
      }
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return result;
  }
}
