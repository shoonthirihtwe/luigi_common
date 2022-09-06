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
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.Luigi2ReceiverEmailInfo.ClientMailType;
import jp.co.ichain.luigi2.resources.Luigi2ReceiverEmailInfo.MailType;
import jp.co.ichain.luigi2.resources.Luigi2ReceiverEmailInfo.ReceiverInfo;
import jp.co.ichain.luigi2.resources.Luigi2ReceiverEmailInfo.TenantMailType;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
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
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  @Autowired
  CommonMapper mapper;

  @Value("${newbusiness.sender.email.clients}")
  String newBusinessClientsSender;

  @Value("${policy.sender.email.clients}")
  String policyClientsSender;

  @Value("${claim.sender.email.clients}")
  String claimClientsSender;

  @Value("${billing.sender.email.clients}")
  String billingClientsSender;

  @Value("${renewal.sender.email.clients}")
  String renewalClientsSender;

  @Value("${sender.email.tenants}")
  String senderEmailTenants;

  /**
   * 直メール発送
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param templateNumber
   * @param to
   * @param paramMap : tenantId必須
   * @param sender
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public void send(String templateNumber, String to, Map<String, Object> paramMap, String sender)
      throws JsonMappingException, JsonProcessingException {

    val contentInfo = serviceInstancesBaseResources
        .get((Integer) paramMap.get("tenantId"), templateNumber + "_notification").get(0);

    // Get body
    Destination destination = new Destination().withToAddresses(to);

    // Get signature
    val appendNotification = serviceInstancesBaseResources
        .get((Integer) paramMap.get("tenantId"), "append_notification").get(0).getInherentMap();

    Message message = new Message()
        .withSubject(new Content().withCharset("UTF-8")
            .withData((String) contentInfo.getInherentMap().get("subject")))
        .withBody(new Body().withHtml(new Content().withCharset("UTF-8")
            .withData(makeBody(contentInfo, appendNotification, paramMap))));

    // Send the email
    client.sendEmail(new SendEmailRequest().withSource(sender).withDestination(destination)
        .withMessage(message));
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
          if (value != null) {
            sbBody.append(value);
          }
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

  /**
   * Sender取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-11-09
   * @updatedAt : 2021-11-09
   * @param ReceiverInfo
   * @param MailType
   * @param paramMap : tenantId, contractNo必須
   * @return
   */
  public String getSender(ReceiverInfo receiverInfo, MailType mailType, Map<String, Object> param) {

    if (receiverInfo == ReceiverInfo.sender_emails_to_tenants) {
      return senderEmailTenants;
    }

    String sender =
        mapper.selectSenderEmailsByContractNo(receiverInfo.name(), mailType.getName(), param);

    if (sender == null && receiverInfo == ReceiverInfo.sender_emails_to_clients) {
      switch (((ClientMailType) mailType)) {
        case new_business:
          sender = newBusinessClientsSender;
          break;
        case policy_management:
          sender = policyClientsSender;
          break;
        case claim:
          sender = claimClientsSender;
          break;
        case billing:
          sender = billingClientsSender;
          break;
        case renewal:
          sender = renewalClientsSender;
          break;
        default:
          break;
      }
    }

    return sender;
  }

  /**
   * オペレータメール取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-11-09
   * @updatedAt : 2021-11-09
   * @param ReceiverInfo
   * @param MailType
   * @param paramMap : tenantId, contractNo必須
   * @return
   */
  public String getTenantsEmail(TenantMailType mailType, Map<String, Object> param) {
    String tenantsEmail = mapper.selectSenderEmailsByContractNo(
        ReceiverInfo.sender_emails_to_tenants.name(), mailType.getName(), param);
    if (tenantsEmail == null) {
      return senderEmailTenants;
    }
    return tenantsEmail;
  }
}
