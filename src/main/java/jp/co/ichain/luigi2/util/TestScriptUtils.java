package jp.co.ichain.luigi2.util;



import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ichain.luigi2.resources.TestSqlResources;

/**
 * TestScriptUtils
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-07-13
 * @updatedAt : 2021-07-13
 */
@Component
public class TestScriptUtils {

  @Autowired
  TestSqlResources testSqlResources;

  @Value("${test.init.sql.path}")
  String testInitDataPath;

  @Autowired
  @Qualifier("luigi2DataSource")
  private DataSource dataSource;

  /**
   * execute Sql
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param src
   * @return
   * @throws SQLException
   * @throws ScriptException
   */
  public void executeSqlScript(String sqlPath) throws ScriptException, SQLException {

    ScriptUtils.executeSqlScript(dataSource.getConnection(),
        new EncodedResource(new ClassPathResource(sqlPath)), true, true, "--", "^;", "/*", "*/");
  }

  /**
   * データベースを初期化
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param connection
   * @return
   * @throws SQLException
   * @throws ScriptException
   */
  public void cleanUpDatabase() throws ScriptException, SQLException {
    ScriptUtils.executeSqlScript(dataSource.getConnection(),
        new EncodedResource(testSqlResources.getSchemaSqlResource()), true, true, "--", "^;", "/*",
        "*/");

    executeSqlScript(testInitDataPath);
  }

  /**
   * JsonFile to MultiValueMap
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param connection
   * @return
   * @throws SQLException
   * @throws ScriptException
   */
  public MultiValueMap<String, String> loadJsonToMultiValueMap(String path)
      throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(path);
    Map<String, String> map =
        mapper.readValue(new InputStreamReader(resource.getInputStream(), "UTF-8"),
            new TypeReference<Map<String, String>>() {});
    params.setAll(map);
    return params;
  }

  /**
   * JsonFile to HashMap
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param connection
   * @return
   * @throws SQLException
   * @throws ScriptException
   */
  public HashMap<String, String> loadJsonToHashMap(String path)
      throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {

    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(path);
    return mapper.readValue(new InputStreamReader(resource.getInputStream(), "UTF-8"),
        new TypeReference<HashMap<String, String>>() {});

  }

  /**
   * JsonFile to String
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param connection
   * @return
   * @throws SQLException
   * @throws ScriptException
   */
  public String loadJsonToString(String path) throws JsonParseException, JsonMappingException,
      JsonProcessingException, UnsupportedEncodingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(path);
    return mapper.writeValueAsString(
        mapper.readValue(new InputStreamReader(resource.getInputStream(), "UTF-8"),
            HashMap.class));
  }

}
