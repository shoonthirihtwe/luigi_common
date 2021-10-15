package jp.co.ichain.luigi2.config.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日付形式変換
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-15
 * @updatedAt : 2021-10-15
 */
public class JsonDateSimpleFormatSerializer extends JsonSerializer<Object> {
  @Override
  public void serialize(Object data, JsonGenerator gen, SerializerProvider arg2)
      throws IOException, JsonProcessingException {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
    if (data != null) {
      if (data instanceof Date) {
        gen.writeString(sdf.format(data));
      } else if (data instanceof String) {
        gen.writeString((String) data);
      }
    }
  }
}
