package jp.co.ichain.luigi2.config;

import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Json形式を変換中、日付形式変換
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-15
 * @updatedAt : 2021-06-15
 */
public class JsonDateSerializer extends JsonSerializer<Date> {
  @Override
  public void serialize(Date data, JsonGenerator gen, SerializerProvider arg2)
      throws IOException, JsonProcessingException {
    if (data != null) {
      gen.writeString("/Date(" + data.getTime() + ")/");
    }
  }
}
