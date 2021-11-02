package jp.co.ichain.luigi2.util;



import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ichain.luigi2.dao.AwsS3Dao;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.mapper.DocumentsMapper;
import lombok.val;

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
  @Qualifier("luigi2DataSource")
  private DataSource dataSource;

  @Autowired
  private CommonMapper commonMapper;

  @Autowired
  private DocumentsMapper documentsMapper;

  @Autowired
  private AwsS3Dao awsS3Dao;

  java.sql.Connection connection;

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
    if (connection == null) {
      connection = dataSource.getConnection();
    }
    ScriptUtils.executeSqlScript(connection, new EncodedResource(new ClassPathResource(sqlPath)),
        true, true, "--", "^;", "/*", "*/");
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
    executeSqlScript("test_sql/data_clear.sql");
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
  @SuppressWarnings("unchecked")
  public MultiValueMap<String, String> loadJsonToMultiValueMap(String path)
      throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(path);
    Map<String, Object> map =
        mapper.readValue(new InputStreamReader(resource.getInputStream(), "UTF-8"),
            new TypeReference<Map<String, Object>>() {});

    for (String key : map.keySet()) {
      if (key.contains("List")) {
        List<String> list = (List<String>) map.get(key);

        for (String str : list) {
          params.add(key, str);
        }
      } else {
        params.add(key, (String) map.get(key));
      }
    }

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
  public Object loadJsonToObject(String path)
      throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {

    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(path);
    return mapper.readValue(new InputStreamReader(resource.getInputStream(), "UTF-8"),
        Object.class);

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
    ClassPathResource resource = new ClassPathResource(path);
    byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
    return new String(bdata, StandardCharsets.UTF_8);

  }

  /**
   * バッチ日付更新
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @param "yyyy-MM-dd"
   * @param tenantId
   * @return
   * @throws ParseException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws SQLException
   * @throws ScriptException
   */
  public void updateBatchDate(String date, Integer... tenantId) throws JsonParseException,
      JsonMappingException, JsonProcessingException, UnsupportedEncodingException, IOException,
      ParseException, InstantiationException, IllegalAccessException, SecurityException {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    val paramMap = new HashMap<String, Object>();
    paramMap.put("batchDate", sdf.parse(date));
    if (tenantId.length != 0) {
      paramMap.put("tenantId", tenantId);
    }
    commonMapper.updateBatchDate(paramMap);

  }

  /**
   * delete s3file
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @return
   */
  public void deleteUploadFiles() {
    val documentList = documentsMapper.selectAllDocumentsUrl("new_business_documents");
    documentList.addAll(documentsMapper.selectAllDocumentsUrl("maintenance_documents"));
    documentList.addAll(documentsMapper.selectAllDocumentsUrl("claim_documents"));

    for (val document : documentList) {
      awsS3Dao.delete(document.getTenantId(), document.getDocumentUrl());
    }
  }

  /**
   * オンライン日付更新
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @param "yyyy-MM-dd"
   * @param tenantId
   * @return
   * @throws ParseException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws SQLException
   * @throws ScriptException
   */
  public void updateOnlineDate(String date, Integer... tenantId) throws JsonParseException,
      JsonMappingException, JsonProcessingException, UnsupportedEncodingException, IOException,
      ParseException, InstantiationException, IllegalAccessException, SecurityException {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    val paramMap = new HashMap<String, Object>();
    paramMap.put("onlineDate", sdf.parse(date));
    if (tenantId.length != 0) {
      paramMap.put("tenantId", tenantId);
    }
    commonMapper.updateOnlineDate(paramMap);
  }
}
