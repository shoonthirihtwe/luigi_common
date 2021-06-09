package jp.co.ichain.luigi2.validity;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import jp.co.ichain.luigi2.vo.ValidityVo;
import lombok.val;

/**
 * パラメーターを検証する
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class Validity {

  /**
   * パラメータータイプ
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   */
  enum VType {
    STRING, BOOL, INT, DATE, FRACTION, OBJECT
  }

  /**
   * パラメーターフォマット
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   */
  enum FormatType {
    EMAIL, TEL
  }

  private final static String VALIDITY_EMAIL =
      "^[0-9a-zA-Z]([-_\\\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\\\.]?[0-9a-zA-Z])*\\\\.[a-zA-Z]{2,3}(.[a-zA-Z]{2,3})?$";
  private final static String VALIDITY_TEL = "^\\\\d{3,4}-?\\\\d{3,4}-?\\\\d{4}$";

  /**
   * service instancesの検証情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param endpoint
   * @return
   */
  public static String getValiditySourceKey(String endpoint) {
    return endpoint + "_validity";
  }



  /**
   * 検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   * @param validityMap
   * @param serviceInstanceMap
   * @param paramMap
   * @param exList
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws UnsupportedEncodingException
   */
  @SuppressWarnings("unchecked")
  public static void validate(Map<String, ValidityVo> validityMap,
      Map<String, Object> serviceInstanceMap, Map<String, Object> paramMap,
      List<WebException> exList)
      throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {

    // validate
    for (val key : serviceInstanceMap.keySet()) {
      val validity = serviceInstanceMap.get(key);
      if ("param-key".equals(validity)) {
        continue;
      }
      val data = paramMap.get(key);

      // type object
      if (validity instanceof Map) {
        val objValidityMap = (Map<String, Object>) validity;
        val validityVo = validityMap.get(objValidityMap.get("param-key"));
        val type = VType.valueOf(validityVo.getType());

        // Required
        if (validityVo.getRequired() && data == null) {
          exList.add(new WebParameterException(Luigi2Code.V0001, key));
        }

        // type validate
        if (type != VType.OBJECT) {
          exList.add(new WebParameterException(Luigi2Code.V0005, key));
        } else if (data != null) {
          // Object recursive call
          if (validityVo.getArray()) {
            if (data instanceof List) {
              List<Map<String, Object>> list = (List<Map<String, Object>>) data;
              for (val map : list) {
                Validity.validate(validityMap, objValidityMap, map, exList);
              }
            } else {
              exList.add(new WebParameterException(Luigi2Code.V0005, key));
            }
          } else {
            if (data instanceof Map) {
              Validity.validate(validityMap, objValidityMap, (Map<String, Object>) data, exList);
            } else {
              exList.add(new WebParameterException(Luigi2Code.V0005, key));
            }
          }
        }
      } else {
        val validityVo = validityMap.get(validity);
        if (validityVo == null) {
          continue;
        }
        if (validityVo.getArray()) {
          if (data instanceof List) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data;
            for (val map : list) {
              Validity.validate(validityVo, serviceInstanceMap, map, exList, key, data);
            }
          } else {
            exList.add(new WebParameterException(Luigi2Code.V0005, key));
          }
        } else {
          Validity.validate(validityVo, serviceInstanceMap, paramMap, exList, key, data);
        }
      }
    }

    if (exList.size() > 0) {
      throw new WebParameterException(Luigi2Code.V0000, exList);
    }
  }

  /**
   * 検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   * @param validityVo
   * @param serviceInstanceMap
   * @param paramMap
   * @param exList
   * @param validity
   * @param key
   * @param data
   * @throws UnsupportedEncodingException
   */
  private static void validate(ValidityVo validityVo, Map<String, Object> serviceInstanceMap,
      Map<String, Object> paramMap, List<WebException> exList, String key, Object data)
      throws UnsupportedEncodingException {
    val type = VType.valueOf(validityVo.getType());

    // Required
    if (validityVo.getRequired() && data == null) {
      exList.add(new WebParameterException(Luigi2Code.V0001, key));
    }

    if (data != null) {
      // type
      if (Validity.validateType(type, data) == false) {
        exList.add(new WebParameterException(Luigi2Code.V0005, key));
      }

      // type is string
      if (VType.STRING.toString().equals(validityVo.getType())) {
        String sData = (String) data;
        int length = sData.getBytes("UTF-8").length;
        // min
        if (validityVo.getMin() != null && validityVo.getMin() > length) {
          exList.add(new WebParameterException(Luigi2Code.V0002, key, validityVo.getMin()));
        }
        // max
        if (validityVo.getMax() != null && validityVo.getMax() < length) {
          exList.add(new WebParameterException(Luigi2Code.V0003, key, validityVo.getMax()));
        }
        // formats
        if (validityVo.getFormats() != null) {
          for (val format : validityVo.getFormats()) {
            if (FORMAT_MAP.get(FormatType.valueOf(format)).apply(sData) == false) {
              exList.add(new WebParameterException(Luigi2Code.V0004, key));
            }
          }
        }
      }

      // fixed
      if (Validity.validiateFixedList(validityVo.getFixedList(), data) == false) {
        exList.add(new WebParameterException(Luigi2Code.V0004, key));
      }
      if (Validity.validiateIntFixedList(validityVo.getIntFixedList(), data) == false) {
        exList.add(new WebParameterException(Luigi2Code.V0004, key));
      }
    }
  }

  /**
   * 属性検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   * @param type
   * @param data
   * @return
   */
  private static boolean validateType(VType type, Object data) {
    switch (type) {
      case STRING:
        return data instanceof String;
      case BOOL:
        return data instanceof Boolean;
      case DATE:
      case INT:
        return data instanceof Integer || data instanceof Long || data instanceof Short
            || data instanceof Byte || data instanceof BigInteger;
      case FRACTION:
        return data instanceof Double || data instanceof Float || data instanceof BigDecimal;
      default:
        return true;
    }
  }

  /**
   * 決められた値検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   * @param fixedList
   * @param data
   * @return
   */
  private static boolean validiateFixedList(List<String> fixedList, Object data) {

    if (fixedList == null || data == null || fixedList.size() == 0) {
      return true;
    }

    for (Object item : fixedList) {
      if (data.equals(item)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 決められた値検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   * @param fixedList
   * @param data
   * @return
   */
  private static boolean validiateIntFixedList(List<Integer> fixedList, Object data) {

    if (fixedList == null || data == null || fixedList.size() == 0) {
      return true;
    }

    for (Object item : fixedList) {
      if (data.equals(item)) {
        return true;
      }
    }
    return false;
  }

  public static Map<FormatType, Function<String, Boolean>> FORMAT_MAP =
      new HashMap<FormatType, Function<String, Boolean>>();
  static {
    /**
     * Emailフォーマット検証
     * 
     * @author : [AOT] s.paku
     * @createdAt : 2021-05-31
     * @updatedAt : 2021-05-31
     * @param value
     * @param field
     * @return
     */
    FORMAT_MAP.put(FormatType.EMAIL, (value) -> {
      if (value == null || value.length() == 0) {
        return true;
      }

      return value.matches(VALIDITY_EMAIL);
    });

    /**
     * Emailフォーマット検証
     * 
     * @author : [AOT] s.paku
     * @createdAt : 2021-05-31
     * @updatedAt : 2021-05-31
     * @param value
     * @param field
     * @return
     */
    FORMAT_MAP.put(FormatType.TEL, (value) -> {
      if (value == null || value.length() == 0) {
        return true;
      }

      return value.matches(VALIDITY_TEL);
    });
  }
}
