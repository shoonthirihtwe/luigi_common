package jp.co.ichain.luigi2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombok.val;

/**
 * Excel Utils
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-26
 * @updatedAt : 2021-08-26
 */
public class ExcelUtils {
  /**
   * Excelを作成する
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-30
   * @updatedAt : 2021-08-30
   * @param titleMap
   * @param dataList
   * @return
   * @throws IOException
   */
  public static ByteArrayOutputStream write(Map<String, String> mappingMap,
      List<Map<String, Object>> dataList, String fileName) throws IOException {
    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet(fileName);
    Row row = null;
    Cell cell = null;
    int rowNum = 0;
    List<String> headerList = mappingMap.keySet().stream().collect(Collectors.toList());
    // Header
    row = sheet.createRow(rowNum++);
    for (int i = 0; i < headerList.size(); i++) {
      cell = row.createCell(i);
      cell.setCellValue(headerList.get(i));
    }

    List<String> listValues = new ArrayList<String>(mappingMap.values());
    for (val data : dataList) {
      row = sheet.createRow(rowNum++);
      int colNum = 0;
      for (val value : listValues) {
        cell = row.createCell(colNum++);
        if (data.get(value) != null) {
          cell.setCellValue(data.get(value).toString());
        }
      }
    }
    val resultOutput = new ByteArrayOutputStream();
    wb.write(resultOutput);
    wb.close();

    return resultOutput;
  }

}
