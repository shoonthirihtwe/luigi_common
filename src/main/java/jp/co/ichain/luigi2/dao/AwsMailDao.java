package jp.co.ichain.luigi2.dao;

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
        .get((Integer) paramMap.get("tenantId"), templateNumber + "_notification").get(0)
        .getInherentMap();

    // Get body
    Destination destination = new Destination().withToAddresses(to);
    val body = (String) contentInfo.get("body");

    // Get signature
    val signature = (String) serviceInstancesResources
        .get((Integer) paramMap.get("tenantId"), "signature_notification").get(0).getInherentMap()
        .get("signature");

    Message message = new Message()
        .withSubject(
            new Content().withCharset("UTF-8").withData((String) contentInfo.get("subject")))
        .withBody(new Body().withHtml(
            new Content().withCharset("UTF-8").withData(makeBody(body, signature, paramMap))));

    // Send the email
    client.sendEmail(new SendEmailRequest().withSource("no-reply@lg2.ichain.co.jp")
        .withDestination(destination).withMessage(message));
  }


  /**
   * メール内容を生成
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param body
   * @param paramMap
   */
  private String makeBody(String body, String signature, Map<String, Object> paramMap) {
    // make body
    class BodyFg {
      boolean keyFg = false;
      boolean dateConvertFg = false;
      boolean keyOpen = false;
    }
    BodyFg bodyFg = new BodyFg();
    val sbBody = new StringBuffer();
    val sbKey = new StringBuffer();

    body.chars().forEach((ci) -> {
      val c = (char) ci;
      if (c == '$') {
        bodyFg.keyFg = true;
        return;
      } else if (bodyFg.keyFg && c == 'D') {
        bodyFg.dateConvertFg = true;
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
    sbBody.append(signature);

    return new String(sbBody);
  }

  AwsMailDao(@Value("${aws.ses.region}") String region,
      @Value("${mail.date.format}") String mailDateFormat,
      AWSCredentialsProvider credentialsProvider) {
    client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(region)
        .withCredentials(credentialsProvider).build();
    dateFormat = new SimpleDateFormat(mailDateFormat);
  }
}
