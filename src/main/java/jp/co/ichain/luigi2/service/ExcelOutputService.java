package jp.co.ichain.luigi2.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.opencsv.exceptions.CsvException;
import jp.co.ichain.luigi2.exception.WebConditionException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.CodeMasterResources;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.resources.Luigi2ErrorMessage.ErrorMessage;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.resources.ServiceInstancesResources;
import jp.co.ichain.luigi2.vo.ApplicationResultVo;
import jp.co.ichain.luigi2.vo.ExcelErrorResultVo;
import lombok.val;

/**
 * ExcelUtils
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-26
 * @updatedAt : 2021-08-26
 */
@Service
public class ExcelOutputService {

  @Autowired
  CodeMasterResources codeMasterResources;

  @Autowired
  CommonService commonService;

  @Autowired
  ServiceInstancesBaseResources serviceInstanceBaseResources;

  @Autowired
  ServiceInstancesResources serviceInstanceResources;

  @Autowired
  ServiceObjectsService serviceObjectService;

  /**
   * パラメータータイプ
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-01
   * @updatedAt : 2021-10-01
   */
  public enum Vtype {
    NUM, STRING, DATE, OBJECT, OBJECT_LIST
  }

  /**
   * Excelデータ取得
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
  public List<Map<String, Object>> get(MultipartFile excelFile, Map<String, Object> matchingData,
      Integer tenantId, String sheetName) throws IOException, CsvException {

    String extension = FilenameUtils.getExtension(excelFile.getOriginalFilename());

    if (!extension.equals("xlsx") && !extension.equals("xls")) {
      throw new WebException(Luigi2ErrorCode.F0003);
    }

    Workbook workbook = null;

    if (extension.equals("xlsx")) {
      workbook = new XSSFWorkbook(excelFile.getInputStream());
    } else if (extension.equals("xls")) {
      workbook = new HSSFWorkbook(excelFile.getInputStream());
    }

    Sheet worksheet = workbook.getSheet(sheetName);
    if (worksheet == null) {
      return null;
    }

    String[] headers = new String[worksheet.getRow(0).getPhysicalNumberOfCells()];

    for (int j = 0; j < headers.length; j++) {

      val cell = worksheet.getRow(0).getCell(j);
      if (cell != null) {
        headers[j] = cell.getStringCellValue();
      }
    }

    List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();

    for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
      Map<String, Cell> dataMap = new HashMap<String, Cell>();
      if (worksheet.getRow(i) == null) {
        continue;
      }
      boolean isEmptyRow = true;
      for (int j = 0; j < worksheet.getRow(i).getLastCellNum(); j++) {

        Cell cell = worksheet.getRow(i).getCell(j);
        if (cell != null && cell.getCellType() != CellType.BLANK) {
          isEmptyRow = false;
        }
        dataMap.put(headers[j], worksheet.getRow(i).getCell(j));
      }
      if (isEmptyRow == false) {
        val resultMap = convert(matchingData, dataMap, tenantId, null);
        resultMapList.add(resultMap);

      }

    }
    return resultMapList;
  }

  /**
   * データマーチングして取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-26
   * @updatedAt : 2021-08-26
   * @param JSONObject jsonObj
   * @param Map<String, Cell> dataMap
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @SuppressWarnings("unchecked")
  private Map<String, Object> convert(Map<String, Object> inputMap, Map<String, Cell> dataMap,
      Integer tenantId, Integer arrayCnt) throws JsonMappingException, JsonProcessingException {

    Map<String, Object> resultMap = new HashMap<String, Object>();

    for (val key : inputMap.keySet()) {
      val data = inputMap.get(key);
      if (data instanceof Map) {
        val insideData = (Map<String, Object>) data;
        val type = insideData.get("type");
        if (type.equals(Vtype.OBJECT.name())) {
          Integer arrayCount = null;
          try {
            arrayCount = (Integer) insideData.get("arrayCount");
          } catch (Exception e) {
            resultMap.put(key, convert(insideData, dataMap, tenantId, null));
          }

          if (arrayCount != null) {
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            for (int i = 1; i <= arrayCount; i++) {
              val result = convert(insideData, dataMap, tenantId, i);
              if (result.size() > 0) {
                listMap.add(result);
              }
            }
            resultMap.put(key, listMap);
          } else {
            resultMap.put(key, convert(insideData, dataMap, tenantId, null));
          }
        } else {
          String headerName = insideData.get("headerName").toString();
          if (headerName != null) {
            if (arrayCnt != null) {
              headerName = String.format(headerName, arrayCnt);
            }
            val value = dataMap.get(headerName);
            if (value != null) {
              // convert CodeMaster
              val codeMaster = insideData.get("codeMaster");
              if (codeMaster != null) {
                String convertedCodeMasterValue = null;
                try {
                  convertedCodeMasterValue = codeMasterResources.getValue(tenantId,
                      codeMaster.toString(), value.getStringCellValue());
                } catch (JsonMappingException e) {
                  e.printStackTrace();
                } catch (JsonProcessingException e) {
                  e.printStackTrace();
                }
                value.setCellValue(convertedCodeMasterValue);
              } else if (insideData.get("inherentEnum") != null) {
                // convert inherentEnum
                val titleMap = serviceInstanceResources.getEnumTitleMap(tenantId, key);
                if (titleMap != null && titleMap.containsKey(value.toString())) {
                  value.setCellValue(titleMap.get(value.toString()));
                }
              }
              if (value != null) {
                val result = convertType(Vtype.valueOf(type.toString()), value);
                if (result != null) {
                  resultMap.put(key, result);
                }
              }

            }
          }
        }
      }
    }
    return resultMap;
  }


  /**
   * AB001申込バリデーションチェック
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-04
   * @updatedAt : 2021-10-04
   * @param csvFileInputStream
   * @param tenantId
   * @param resultMapList
   * @param endpoint
   * @return
   * @throws IOException
   * @throws CsvException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @SuppressWarnings("unchecked")
  public List<ExcelErrorResultVo> validateApplyParam(Integer tenantId,
      List<Map<String, Object>> mapList, String endPoint) throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, IOException, CsvException {

    val size = mapList.size();
    List<ExcelErrorResultVo> errorList = new ArrayList<ExcelErrorResultVo>();
    for (int i = 0; i < size; i++) {

      val lineMap = mapList.get(i);

      val num = i + 1;
      try {
        commonService.validate(lineMap, endPoint);

      } catch (WebParameterException | WebConditionException e) {

        e.getErrArgs().forEach(err -> {
          ExcelErrorResultVo error = new ExcelErrorResultVo();
          val exception = (WebException) err;
          error.setErrArgs((List<Object>) (exception).getErrArgs());
          error.setParentKey(exception.getParentKey());
          error.setArrayIndex(exception.getArrayIndex());
          error.setCode(((WebException) err).getCode().toString());
          error.setLineNumber(num);
          errorList.add(error);
        });
      } catch (Exception e) {
        e.printStackTrace();
        ExcelErrorResultVo error = new ExcelErrorResultVo();
        error.setCode(Luigi2ErrorCode.V0000);
        error.setLineNumber(num);
        errorList.add(error);
      }

      try {
        lineMap.put("inherentList", setInherentData((Map<String, Object>) lineMap.get("data")));
        serviceObjectService.validateInherentList(tenantId,
            (List<Map<String, Object>>) lineMap.get("inherentList"));
      } catch (WebParameterException | WebConditionException e) {

        e.getErrArgs().forEach(err -> {
          ExcelErrorResultVo error = new ExcelErrorResultVo();
          val exception = (WebException) err;
          error.setErrArgs((List<Object>) (exception).getErrArgs());
          error.setParentKey(exception.getParentKey());
          error.setArrayIndex(exception.getArrayIndex());
          error.setCode(((WebException) err).getCode().toString());
          error.setLineNumber(num);
          errorList.add(error);
        });
      } catch (Exception e) {
        e.printStackTrace();
        ExcelErrorResultVo error = new ExcelErrorResultVo();
        error.setCode(Luigi2ErrorCode.V0000);
        error.setLineNumber(num);
        errorList.add(error);
      }
    }
    return errorList;
  }

  private List<Map<String, Object>> setInherentData(Map<String, Object> data) {
    val inherent = new HashMap<String, Object>();
    inherent.put("txType", "C");
    inherent.put("sequenceNo", 1);
    inherent.put("inherent", data == null ? "{}" : data);
    val inherentList = new ArrayList<Map<String, Object>>();
    inherentList.add(inherent);
    return inherentList;
  }

  /**
   * Excel 値type変更
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-05
   * @updatedAt : 2021-10-05
   * @param type
   * @param value
   * @return object
   */

  private Object convertType(Vtype type, Cell value) {
    try {
      Object result = null;
      switch (value.getCellType().name()) {
        case "NUMERIC":
          result = (long) value.getNumericCellValue();
          break;
        case "STRING":
          result = value.getStringCellValue();
          break;
        case "BLANK":
          return null;
        default:
          return null;
      }

      switch (type.name()) {
        case "NUM":
          return Long.valueOf(result.toString());
        case "STRING":
          return result.toString();
        case "DATE":
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          if (value.getCellType() == CellType.NUMERIC) {
            return sdf.format(value.getDateCellValue());
          }
          return value.getStringCellValue();

        default:
          return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * エラー結果を作成
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-05
   * @updatedAt : 2021-10-05
   * @param titleMap
   * @param dataList
   * @return
   * @throws IOException
   */
  public ResponseEntity<Resource> writeError(List<ExcelErrorResultVo> errorList,
      Map<String, Object> matchingData, Integer tenantId) throws IOException {

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet("エラー結果");
    Row row = null;
    Cell cell = null;
    int rowNum = 0;

    // Header
    row = sheet.createRow(rowNum++);

    cell = row.createCell(0);
    cell.setCellValue("エラー箇所（行）");
    sheet.autoSizeColumn(0);
    cell = row.createCell(1);
    cell.setCellValue("エラー内容");
    sheet.autoSizeColumn(1);

    for (val errorResult : errorList) {
      if (convertErrorArgToHeader(errorResult, matchingData) == false) {
        continue;
      }

      row = sheet.createRow(rowNum++);
      cell = row.createCell(0);
      cell.setCellValue(errorResult.getLineNumber().toString());
      cell = row.createCell(1);
      cell.setCellValue(getParameterErrorMessage(errorResult));
    }

    val resultOutput = new ByteArrayOutputStream();
    wb.write(resultOutput);
    wb.close();
    return outputStreamToResource(resultOutput, "errorResult");
  }

  /**
   * エラー結果を作成
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-05
   * @updatedAt : 2021-10-05
   * @param titleMap
   * @param dataList
   * @return
   * @throws IOException
   */
  public ResponseEntity<Resource> writeResult(List<ApplicationResultVo> applicationList,
      MultipartFile excelFile) throws IOException {

    String extension = FilenameUtils.getExtension(excelFile.getOriginalFilename());

    if (!extension.equals("xlsx") && !extension.equals("xls")) {
      throw new WebException(Luigi2ErrorCode.F0003);
    }

    Workbook workbook = null;
    Sheet sheet = null;
    if (extension.equals("xlsx")) {
      workbook = new XSSFWorkbook(excelFile.getInputStream());
      sheet = workbook.getSheetAt(0);
      ((XSSFSheet) sheet).getCTWorksheet().setDataValidations(null);

    } else if (extension.equals("xls")) {
      workbook = new HSSFWorkbook(excelFile.getInputStream());
      sheet = workbook.getSheetAt(0);
    }

    // header Cell Style
    CellStyle cellStyle = workbook.createCellStyle();
    for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
      if (sheet.getRow(0).getCell(j) != null) {
        sheet.getRow(0).getCell(j).setCellStyle(cellStyle);
      }
    }

    // 追加行出力
    if (applicationList != null && applicationList.size() > 0) {
      sheet.shiftColumns(0, sheet.getRow(0).getPhysicalNumberOfCells(), 2);
      Row row = null;
      Cell cell = null;
      int rowNum = 0;

      // Header
      row = sheet.getRow(rowNum++);
      cell = row.createCell(0);
      cell.setCellValue("申込行");
      cell = row.createCell(1);
      cell.setCellValue("証券番号");

      for (val applyResult : applicationList) {
        row = sheet.getRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue(String.valueOf(rowNum - 1));
        cell = row.createCell(1);
        cell.setCellValue(applyResult.getContractNo());
      }
    }

    val resultOutput = new ByteArrayOutputStream();
    workbook.write(resultOutput);

    return outputStreamToResource(resultOutput, "applyResult");
  }

  /**
   * OutputStreamをResourceに変更
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-05
   * @updatedAt : 2021-10-05
   * @param ByteArrayOutputStream resultOutput
   * @return ResponseEntity<Resource>
   */
  private ResponseEntity<Resource> outputStreamToResource(
      final java.io.ByteArrayOutputStream resultOutput, String fileName) throws IOException {
    val s3stream = new ByteArrayInputStream(resultOutput.toByteArray());
    val result = new ByteArrayResource(IOUtils.toByteArray(s3stream));

    return ResponseEntity.ok()
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=%s", fileName + ".xlsx"))
        .contentType(MediaType.valueOf(new Tika().detect(s3stream))).body(result);
  }

  /**
   * エラーパラメータを対象ヘッダー名に変更
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-05
   * @updatedAt : 2021-10-05
   * @param ExcelErrorResultVo errorVo
   * @param Map<String, Object> matchingData
   * @return
   */
  @SuppressWarnings("unchecked")
  private boolean convertErrorArgToHeader(ExcelErrorResultVo errorVo,
      Map<String, Object> matchingData) {

    final String parentKey = errorVo.getParentKey();
    final Integer arrayIndex = errorVo.getArrayIndex();
    if (errorVo.getErrArgs() == null) {
      return false;
    }
    val arg = (String) errorVo.getErrArgs().get(0);
    String convertedArg = "";
    if (parentKey == null && matchingData.get(arg) instanceof String) {
      convertedArg = (String) matchingData.get(arg);
    } else if (parentKey != null) {

      convertedArg = (String) ((Map<String, Object>) matchingData.get(parentKey)).get(arg);
    } else {
      convertedArg = findChildMatchingMap(arg, matchingData);
    }
    if (arrayIndex != null) {
      convertedArg = String.format(convertedArg, arrayIndex + 1);
    }

    errorVo.getErrArgs().set(0, convertedArg);
    return true;
  }

  /**
   * parentKeyがない場合、下のMapでマッチングを探索
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-13
   * @updatedAt : 2022-09-13
   * @param String arg
   * @param Map<String, Object> matchingData
   * @return
   */
  @SuppressWarnings("unchecked")
  private String findChildMatchingMap(String arg, Map<String, Object> map) {
    for (val key : map.keySet()) {
      val obj = map.get(key);
      if (obj instanceof Map) {
        val childMap = (Map<String, Object>) obj;
        if (childMap.containsKey(arg)) {
          return childMap.get(arg).toString();
        }
      }
    }
    return arg;
  }

  /**
   * errorMessage取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-05
   * @updatedAt : 2021-10-05
   * @param src
   * @return
   */
  private String getParameterErrorMessage(ExcelErrorResultVo error) {

    val msg = ErrorMessage.valueOf(error.getCode());

    if (msg != null && error.getErrArgs() != null) {
      return String.format(msg.toString(), error.getErrArgs().toArray());
    }

    return ErrorMessage.valueOf(Luigi2ErrorCode.V0000).toString();
  }

}
