package jp.co.ichain.luigi2.resources;

import java.io.IOException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Policy Search Mappingリソース
 *
 * @author : [AOT] s.park
 * @createdAt : 2021-02-19
 * @updatedAt : 2021-03-04
 */
@Singleton
@Service
public class ErrorResources {

  private Properties properties = null;

  @Value("classpath:common/error.properties")
  Resource resourceFile;

  /**
   * エラーメッセージリソースを初期化
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @throws IOException
   */
  @Lock(LockType.WRITE)
  @PostConstruct
  public void initialize() throws IOException {
    properties = new Properties();
    properties.load(resourceFile.getInputStream());
  }

  /**
   * エラーメッセージを取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param code
   * @return
   * @throws IOException
   */
  public String get(String code) throws IOException {
    if (code.length() > 4) {
      return this.properties.getProperty(code.substring(0, 4));
    } else {
      return this.properties.getProperty(code);
    }
  }
}
