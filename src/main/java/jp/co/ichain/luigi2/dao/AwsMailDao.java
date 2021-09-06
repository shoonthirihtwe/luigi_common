package jp.co.ichain.luigi2.dao;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.resources.ServiceInstancesResources;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import lombok.val;

/**
 * Mail Dao
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-13
 * @updatedAt : 2021-07-13
 */
@Service
public class AwsMailDao {


  private final SimpleDateFormat dateFormat;

  private final AmazonSimpleEmailService client;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

  /**
   * 直メール発送
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param templateNumber
   * @param to
   * @param paramMap : tenantId必須
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public void send(String templateNumber, String to, Map<String, Object> paramMap)
      throws JsonMappingException, JsonProcessingException {

    val contentInfo = serviceInstancesResources
        .get((Integer) paramMap.get("tenantId"), templateNumber + "_notification").get(0);

    // Get body
    Destination destination = new Destination().withToAddresses(to);

    // Get signature
    val appendNotification = serviceInstancesResources
        .get((Integer) paramMap.get("tenantId"), "append_notification").get(0).getInherentMap();

    Message message = new Message()
        .withSubject(new Content().withCharset("UTF-8")
            .withData((String) contentInfo.getInherentMap().get("subject")))
        .withBody(new Body().withHtml(new Content().withCharset("UTF-8")
            .withData(makeBody(contentInfo, appendNotification, paramMap))));

    // Send the email
    // TODO s.paku 発信元Email決まったら変更
    client.sendEmail(new SendEmailRequest().withSource("no-reply@lg2.ichain.co.jp")
        .withDestination(destination).withMessage(message));
  }


  /**
   * メール内容を生成
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param contentInfo
   * @param paramMap
   */
  private String makeBody(ServiceInstancesVo contentInfo, Map<String, Object> appendNotification,
      Map<String, Object> paramMap) {
    // make body
    class BodyFg {
      boolean keyFg = false;
      boolean keyOpen = false;
      boolean dateConvertFg = false;
      boolean currencyConvertFg = false;
    }

    val bodyFg = new BodyFg();
    val sbBody = new StringBuffer();
    val sbKey = new StringBuffer();

    Boolean refundYn = (Boolean) contentInfo.getInherentMap().get("refundInfo");
    if (refundYn != null && refundYn) {
      paramMap.put("refundInfo", appendNotification.get("refundInfo"));
    }

    Boolean mypageYn = (Boolean) contentInfo.getInherentMap().get("mypageUrl");
    if (mypageYn != null && mypageYn) {
      paramMap.put("mypageUrl", appendNotification.get("mypageUrl"));
    }

    contentInfo.getInherentText().chars().forEach((ci) -> {
      val c = (char) ci;
      if (c == '\r') {
        sbBody.append("<br/>");
        return;
      } else if (c == '\n') {
        return;
      } else if (c == '$') {
        bodyFg.keyFg = true;
        return;
      } else if (bodyFg.keyFg && !bodyFg.keyOpen && !bodyFg.dateConvertFg && c == 'D') {
        bodyFg.dateConvertFg = true;
        return;
      } else if (bodyFg.keyFg && !bodyFg.keyOpen && !bodyFg.currencyConvertFg && c == 'Y') {
        bodyFg.currencyConvertFg = true;
        return;
      } else if (bodyFg.keyFg && c == '{') {
        bodyFg.keyOpen = true;
        return;
      } else if (bodyFg.keyFg && bodyFg.keyOpen && c == '}') {
        val value = paramMap.get(new String(sbKey));
        if (bodyFg.dateConvertFg) {
          if (value != null) {
            sbBody.append(dateFormat.format(new Date((long) value)));
          }
          bodyFg.dateConvertFg = false;
        } else if (bodyFg.currencyConvertFg) {
          if (value != null) {
            sbBody.append(String.format("%,d円", value));
          }
          bodyFg.currencyConvertFg = false;
        } else {
          sbBody.append(value);
        }
        sbKey.setLength(0);
        bodyFg.keyOpen = false;
        bodyFg.keyFg = false;
        return;
      } else if (bodyFg.keyFg && bodyFg.keyOpen) {
        sbKey.append(c);
        return;
      } else if (bodyFg.keyFg && !bodyFg.keyOpen) {
        sbBody.append('$');
        if (bodyFg.dateConvertFg) {
          sbBody.append('D');
          bodyFg.dateConvertFg = false;
        }
        sbBody.append(c);
        bodyFg.keyFg = false;
        return;
      } else {
        sbBody.append(c);
      }
    });

    // 署名
    sbBody.append(appendNotification.get("signature"));

    return new String(sbBody);
  }

  AwsMailDao(@Value("${aws.ses.region}") String region,
      @Value("${mail.date.format}") String mailDateFormat,
      AWSCredentialsProvider credentialsProvider) throws UnsupportedEncodingException {
    client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(region)
        .withCredentials(credentialsProvider).build();
    dateFormat = new SimpleDateFormat(new String(mailDateFormat.getBytes("ISO-8859-1"), "UTF-8"));
  }
}
