package jp.co.ichain.luigi2.dao;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

/**
 * Slack Dao
 *
 * @author : [AOT] g.kim
 * @createdAt : 2022-10-27
 * @updatedAt : 2022-10-27
 */
@Service
public class SlackDao {

  @Value("${settings.slack.webwookUrl}")
  String webwookUrl;

  /**
   * send message
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param text
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @SuppressWarnings("deprecation")
  public WebhookResponse send(String userName, String text) {
    try {
      WebhookResponse response = null;
      Slack slack = Slack.getInstance();
      Payload payload = Payload.builder().username(userName).text(text).build();
      response = slack.send(webwookUrl, payload);
      return response;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
