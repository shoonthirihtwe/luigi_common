package jp.co.ichain.luigi2.config.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Json形式に変換中、日付形式変換
 *
 * @author : [AOT] s.park
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class JsonDateDeserializer extends JsonDeserializer<Object> {
  @Override
  public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationcontext)
      throws IOException, JsonProcessingException {
    String dateStr = jsonParser.getText();
    Matcher mat = Pattern.compile("/Date((¥d*))/").matcher(dateStr);
    mat.find();

    if (dateStr != null) {
      return new Date(Long.parseLong(mat.group()));
    } else {
      return null;
    }
  }
}
