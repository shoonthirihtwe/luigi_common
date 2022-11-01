package jp.co.ichain.luigi2.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.tika.Tika;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.amazonaws.util.IOUtils;
import lombok.val;

/**
 * FileOutputUtils
 *
 * @author : [AOT] g.kim
 * @createdAt : 2022-11-01
 * @updatedAt : 2022-11-01
 * @param
 * @return
 */
public class FileOutputUtils {

  public static enum FileType {
    excel(".xlsx"), csv(".csv");

    String val;

    FileType(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * writeFile
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-11-01
   * @updatedAt : 2022-11-01
   * @param
   * @return
   */
  public static ResponseEntity<Resource> writeFile(Map<String, String> mappingMap,
      List<Map<String, Object>> dataList, String fileName, FileType fileType) throws IOException {

    ByteArrayOutputStream outputStream = null;
    switch (fileType) {
      case excel:
        outputStream = ExcelUtils.write(mappingMap, dataList, fileName);
        break;
      case csv:
        outputStream = CsvUtils.write(mappingMap, dataList, true);
        break;
      default:
        return null;
    }

    val s3stream = new ByteArrayInputStream(outputStream.toByteArray());
    val result = new ByteArrayResource(IOUtils.toByteArray(s3stream));

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=%s", fileName + fileType.toString()))
        .contentType(MediaType.valueOf(new Tika().detect(s3stream))).body(result);
  }
}
