package jp.co.ichain.luigi2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
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

    CSVReader reader =
        new CSVReader(new InputStreamReader(new BOMInputStream(csvFile.getInputStream(), false)));

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
      if (lines.length != headers.length) {
        continue;
      }
      Map<String, Object> map = new HashMap<String, Object>();
      for (int j = 0; j < headers.length; j++) {
        map.put(headers[j], lines[j]);
      }
      csvMapList.add(map);
    }

    reader.close();

    return csvMapList;
  }

  /**
   * Csvを作成する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-30
   * @updatedAt : 2021-08-30
   * @param titleMap
   * @param dataList
   * @return
   * @throws IOException
   */
  public static ByteArrayOutputStream write(Map<String, String> titleMap,
      List<Map<String, Object>> dataList) throws IOException {
    val result = new ByteArrayOutputStream();
    val keys = titleMap.keySet();
    val convert = new Convert();

    // BOM
    result.write(0xEF);
    result.write(0xBB);
    result.write(0xBF);

    try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(result))) {
      // title writer
      csvWriter.writeNext(titleMap.values().toArray(String[]::new));

      for (val dataMap : CollectionUtils.safe(dataList)) {
        int idx = 0;
        val lines = new String[keys.size()];

        for (val key : CollectionUtils.safe(keys)) {
          lines[idx] = convert.toString(dataMap.get(key));
          idx++;
        }
        csvWriter.writeNext(lines);
      }

    }
    return result;
  }

  /**
   * Csvを作成する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-09-02
   * @updatedAt : 2021-09-02
   * @param titleMap
   * @param dataList
   * @param titleKeyFg
   * @return
   * @throws IOException
   */
  public static ByteArrayOutputStream write(Map<String, String> titleMap,
      List<Map<String, Object>> dataList, boolean titleKeyFg) throws IOException {
    val mappingMap = new LinkedHashMap<String, String>();
    if (titleMap != null && titleKeyFg) {
      for (val entry : titleMap.entrySet()) {
        mappingMap.put(entry.getValue(), entry.getKey().trim());
      }
    }
    return write(mappingMap, dataList);
  }
}
