package jp.co.ichain.luigi2.batch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.vo.TenantsVo;

public interface BatchService {

  public void run(List<TenantsVo> tenantList)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      JsonMappingException, JsonProcessingException, ParseException,
      UnsupportedEncodingException, IOException;
}
