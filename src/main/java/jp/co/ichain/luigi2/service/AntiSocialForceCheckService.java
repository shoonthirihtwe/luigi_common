package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeAntiSocialForceCheck.RetrievalMethod;
import jp.co.ichain.luigi2.util.DateTimeUtils;
import jp.co.ichain.luigi2.vo.AntiSocialForceCheckVo;

/**
 * 反社会的勢力チェックサービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-28
 * @updatedAt : 2021-06-28
 */
@Service
public class AntiSocialForceCheckService {

  @Value("${antisocial.url}")
  String antisocialUrl;

  @Value("${antisocial.x-api-key}")
  String apiKey;

  private static String ANTISOCIAL_DATE = "2019-11-03";

  /**
   * 反社会的勢力チェック
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-11
   * @updatedAt : 2021-08-11
   * @param tenantsId
   * @param name
   * @param birtday
   * @param address
   * @return
   * @throws IOException
   * @throws ClientProtocolException
   */
  public AntiSocialForceCheckVo antisocialCheck(Integer tenantsId, String name, Date birtday,
      String address) throws ClientProtocolException, IOException {
    return this.antisocialCheck(tenantsId, name, birtday, address,
        RetrievalMethod.ACCORD_ALL.toString());
  }

  /**
   * 反社会的勢力チェック
   * 
   * @param $name
   * @param $birthday
   * @param $address
   * @return \stdClass 0：該当なし 4: 漢字氏名のみ一致 5: 漢字氏名、住所が一致 6: 漢字氏名、生年月日が一致 7: 完全一致(漢字氏名、生年月日、住所が一致) 99:
   *         DB取得最大件数
   * @throws IOException
   * @throws UnsupportedOperationException
   */
  public AntiSocialForceCheckVo antisocialCheck(Integer tenantsId, String name, Date birtday,
      String address, String retrievalMethod) throws ClientProtocolException, IOException {
    Map<String, String> inputMap = new HashMap<String, String>();
    inputMap.put("InsurerCodeSeq", String.format("%012d", tenantsId));
    inputMap.put("InsurerInceptionDate", ANTISOCIAL_DATE);
    inputMap.put("RetrievalMethod", retrievalMethod);
    inputMap.put("NameKanji", name);
    inputMap.put("Address", address);
    if (birtday != null) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate dateOfBirth = DateTimeUtils.convertDateToLocalDate(birtday);
      String dateOfbirthday = dateOfBirth.format(formatter);
      inputMap.put("DOB", dateOfbirthday);
    }
    Gson gsonObj = new Gson();
    // convert map to JSON String
    String jsonStr = gsonObj.toJson(inputMap);

    HttpPost httpPost = new HttpPost(antisocialUrl);
    CloseableHttpClient client = HttpClients.createDefault();

    StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity);
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("x-api-key", apiKey);
    CloseableHttpResponse response = client.execute(httpPost);
    String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

    // Convert JSON File to Java Object
    String convertBody = "";
    Boolean flagQuotesFlag = true;
    for (int i = 0; i < body.length(); i++) {
      char text = body.charAt(i);
      if (text == ',' || text == '{') {
        flagQuotesFlag = true;
      }
      if ('A' <= text && text <= 'Z' && flagQuotesFlag) {
        convertBody += Character.toLowerCase(text);
        flagQuotesFlag = false;
      } else {
        convertBody += text;
      }
    }
    AntiSocialForceCheckVo asfcheck = gsonObj.fromJson(convertBody, AntiSocialForceCheckVo.class);
    client.close();
    return asfcheck;
  }
}
