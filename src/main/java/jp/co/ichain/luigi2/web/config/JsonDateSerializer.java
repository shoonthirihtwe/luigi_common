package jp.co.ichain.luigi2.web.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Date;

/**
 * Json形式を変換中、日付形式変換
 *
 * @author : [AOT] s.park
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class JsonDateSerializer extends JsonSerializer<Object> {
  @Override
  public void serialize(Object data, JsonGenerator gen, SerializerProvider arg2)
      throws IOException, JsonProcessingException {
    if (data != null) {
      if (data instanceof Date) {
        gen.writeString("/Date(" + ((Date) data).getTime() + ")/");
      } else if (data instanceof String) {
        gen.writeString((String) data);
      }
    }
  }
}
