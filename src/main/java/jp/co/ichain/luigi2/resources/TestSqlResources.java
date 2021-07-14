package jp.co.ichain.luigi2.resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * TestSqlResources
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-07-13
 * @updatedAt : 2021-07-13
 */
@Singleton
@Service
public class TestSqlResources {

  private Resource schemaSqlResource;

  /**
   * 初期化
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   */
  @PostConstruct
  public void initialize() {

    String line = "";
    String result = "";
    try {
      BufferedReader br =
          new BufferedReader(new FileReader(new ClassPathResource("LG2_DDL.sql").getFile()));

      while ((line = br.readLine()) != null) {
        if (line.startsWith("END$$")) {
          line = "END ^;";
        } else if (line.indexOf("$$") != -1) {
          line = line.replace("$$", ";");
        }
        if (line.startsWith("DELIMITER")) {
          continue;
        }

        result += line + "\r\n";

      }
      br.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.schemaSqlResource = new ByteArrayResource(result.getBytes());

  }

  /**
   * schemaSqlリソース取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   */
  public Resource getSchemaSqlResource() {
    if (this.schemaSqlResource == null) {
      this.initialize();
    }

    return this.schemaSqlResource;
  }
}
