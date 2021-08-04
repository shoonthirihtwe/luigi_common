package jp.co.ichain.luigi2.vo;

import java.util.HashMap;
import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("serial")
public class VoHashMap extends HashMap<Object, Object> {

  @Override
  public Object put(Object key, Object value) {
    if (((String) key).indexOf("_") > 0) {
      return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
    } else {
      return super.put(key, value);
    }

  }
}
