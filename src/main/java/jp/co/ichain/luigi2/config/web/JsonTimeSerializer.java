package jp.co.ichain.luigi2.config.web;

import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Json形式を変換中、時間形式変換
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-28
 * @updatedAt : 2021-06-28
 */
public class JsonTimeSerializer extends JsonSerializer<Object> {
  @Override
  public void serialize(Object data, JsonGenerator gen, SerializerProvider arg2)
      throws IOException, JsonProcessingException {
    if (data != null) {
      if (data instanceof Date) {
        gen.writeString("/Time(" + ((Date) data).getTime() + ")/");
      } else if (data instanceof String) {
        gen.writeString((String) data);
      }
    }
  }
}
