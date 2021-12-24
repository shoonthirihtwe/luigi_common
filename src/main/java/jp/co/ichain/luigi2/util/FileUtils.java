package jp.co.ichain.luigi2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.stereotype.Component;

/**
 * FileUtils
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-07-13
 * @updatedAt : 2021-07-13
 */
@Component
public class FileUtils {

  /**
   * read textFile
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-13
   * @updatedAt : 2021-07-13
   * @param src
   * @return
   * @throws SQLException
   * @throws ScriptException
   */
  public static String readTextFile(InputStream inputStream, boolean removeBom) throws IOException {

    ByteArrayOutputStream expectedFile = new ByteArrayOutputStream();
    inputStream.transferTo(expectedFile);

    byte[] readedBytes = null;
    byte[] data = expectedFile.toByteArray();

    if ((data[0] & 0xFF) == 0xEF && (data[1] & 0xFF) == 0xBB && (data[2] & 0xFF) == 0xBF
        && removeBom) {

      int len = data.length - 3;
      readedBytes = new byte[len];
      System.arraycopy(data, 3, readedBytes, 0, len);

    } else {
      readedBytes = data;
    }

    return new String(readedBytes, "utf-8");
  }

}
