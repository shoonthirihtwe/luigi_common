package jp.co.ichain.luigi2.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import lombok.val;

/**
 * csv Utils
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-26
 * @updatedAt : 2021-08-26
 */
public class CsvUtils {


  /**
   * csvデータ取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-26
   * @updatedAt : 2021-08-26
   * @param MultipartFile csvFile
   * @param Map<title, mapping> matchingData
   * @return
   * @throws IOException 
   * @throws CsvException 
   */
  public static List<Map<String, Object>> get(MultipartFile csvFile,
      Map<String, String> matchingData) throws IOException, CsvException {

    CSVReader reader = new CSVReader(new InputStreamReader(csvFile.getInputStream()));

    val csvLineList = reader.readAll();

    if (csvLineList == null || csvLineList.size() < 2) {
      throw new WebException(Luigi2ErrorCode.F0003);
    }
    String[] headers = csvLineList.get(0);

    for (int i = 0; i < headers.length; i++) {
      val matched = matchingData.get(headers[i]);
      if (matched != null) {
        headers[i] = matched;
      }
    }

    val size = csvLineList.size();
    List<Map<String, Object>> csvMapList = new ArrayList<Map<String, Object>>();
    for (int i = 1; i < size; i++) {
      val lines = csvLineList.get(i);

      Map<String, Object> map = new HashMap<String, Object>();
      for (int j = 0; j < headers.length; j++) {
        map.put(headers[j], lines[j]);
      }
      csvMapList.add(map);
    }

    reader.close();

    return csvMapList;
  }
}
