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
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
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

  private static String ANTISOCIAL_URL = "https://dev.api.dis.ichain.co.jp/api/antiCompany";

  private static String ANTISOCIAL_CODE = "ab1234567890";

  private static String ANTISOCIAL_DATE = "2019-11-03";

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
  public static AntiSocialForceCheckVo antisocialCheck(String name, Date birtday, String address)
      throws ClientProtocolException, IOException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dateOfBirth = DateTimeUtils.convertDateToLocalDate(birtday);
    String dateOfbirthday = dateOfBirth.format(formatter);

    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(ANTISOCIAL_URL);

    Gson gsonObj = new Gson();
    Map<String, String> inputMap = new HashMap<String, String>();
    inputMap.put("InsurerCodeSeq", ANTISOCIAL_CODE);
    inputMap.put("InsurerInceptionDate", ANTISOCIAL_DATE);
    inputMap.put("RetrievalMethod", "0");
    inputMap.put("NameKanji", name);
    inputMap.put("DOB", dateOfbirthday);
    inputMap.put("Address", address);

    // convert map to JSON String
    String jsonStr = gsonObj.toJson(inputMap);

    StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity);
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("x-api-key", "8FXt7noqvF7umEYS4xfb11QNgpZ8fr1g3GHtkgQt");
    CloseableHttpResponse response = client.execute(httpPost);
    String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    // Convert JSON File to Java Object
    AntiSocialForceCheckVo asfcheck = gsonObj.fromJson(body, AntiSocialForceCheckVo.class);
    client.close();
    System.out.println(asfcheck.getResultReords().get(0).getAge());

    return asfcheck;
  }
}
