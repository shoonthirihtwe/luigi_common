package jp.co.ichain.luigi2.validity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebConditionException;
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
@Service
public class Validity {

  @Autowired
  CommonCondition commonCondition;

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
    EMAIL, TEL, HIRA, NUM, ZENNUM, KANA, HANKANA, KANJI, ALPHA
  }

  private final Map<String, String> FORMAT_REGEX_MAP;
  {
    FORMAT_REGEX_MAP = new HashMap<String, String>();
    FORMAT_REGEX_MAP.put("EMAIL",
        "^[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+(\\.[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+)*@[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+(\\.[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+)*$");
    FORMAT_REGEX_MAP.put("TEL", "^\\d{2,4}-\\d{3,4}-\\d{4}$");
    FORMAT_REGEX_MAP.put("HIRA", "\\u3040-\\u309F");
    FORMAT_REGEX_MAP.put("NUM", "0-9");
    FORMAT_REGEX_MAP.put("ZENNUM", "０-９");
    FORMAT_REGEX_MAP.put("KANA", "\\u30a0-\\u30ff");
    FORMAT_REGEX_MAP.put("HANKANA", "\\uFF65-\\uFF9F");
    FORMAT_REGEX_MAP.put("KANJI", "\\u4E00-\\u9FFF");
    FORMAT_REGEX_MAP.put("ALPHA", "a-zA-Z");
  }


  /**
   * service instancesの検証情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param endpoint
   * @return
   */
  public String getValiditySourceKey(String endpoint) {
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
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @SuppressWarnings("unchecked")
  public void validate(Map<String, ValidityVo> validityMap, Map<String, Object> serviceInstanceMap,
      Map<String, Object> paramMap, List<WebException> exList)
      throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // validate
    for (val key : serviceInstanceMap.keySet()) {
      val validity = serviceInstanceMap.get(key);
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

        // Condition
        validateCondition(validityVo, key, data, exList);

        // type validate
        if (type != VType.OBJECT) {
          exList.add(new WebParameterException(Luigi2Code.V0005, key));
        } else if (data != null) {
          // Object recursive call
          if (validityVo.getArray()) {
            if (data instanceof List) {
              List<Map<String, Object>> list = (List<Map<String, Object>>) data;
              for (val map : list) {
                validate(validityMap, objValidityMap, map, exList);
              }
            } else {
              exList.add(new WebParameterException(Luigi2Code.V0005, key));
            }
          } else {
            if (data instanceof Map) {
              validate(validityMap, objValidityMap, (Map<String, Object>) data, exList);
            } else {
              exList.add(new WebParameterException(Luigi2Code.V0005, key));
            }
          }
        }
      } else if ("param-key".equals(key) == false) {
        val validityVo = validityMap.get(validity);
        // Condition
        validateCondition(validityVo, key, data, exList);

        if (validityVo.getArray()) {
          if (data instanceof List) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data;
            for (val map : list) {
              validate(validityVo, serviceInstanceMap, map, exList, key, data);
            }
          } else {
            exList.add(new WebParameterException(Luigi2Code.V0005, key));
          }
        } else {
          validate(validityVo, serviceInstanceMap, paramMap, exList, key, data);
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
  private void validate(ValidityVo validityVo, Map<String, Object> serviceInstanceMap,
      Map<String, Object> paramMap, List<WebException> exList, String key, Object data)
      throws UnsupportedEncodingException {
    val type = VType.valueOf(validityVo.getType());

    // Required
    if (validityVo.getRequired() && data == null) {
      exList.add(new WebParameterException(Luigi2Code.V0001, key));
    }

    if (data != null) {
      // type
      if (validateType(type, data) == false) {
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
          validateFormat(key, validityVo.getFormats(), sData, exList);
        }
      }

      // fixed
      if (validiateFixedList(validityVo.getFixedList(), data) == false) {
        exList.add(new WebParameterException(Luigi2Code.V0004, key));
      }
      if (validiateIntFixedList(validityVo.getIntFixedList(), data) == false) {
        exList.add(new WebParameterException(Luigi2Code.V0004, key));
      }
    }

  }

  /**
   * 固有条件検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param validityVo
   * @param data
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @SuppressWarnings("unchecked")
  private void validateCondition(ValidityVo validityVo, String key, Object data,
      List<WebException> exList)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    // Common Condition
    if (validityVo.getCondition() != null) {
      val conditionMap = validityVo.getCondition();

      for (String conditionMethod : conditionMap.keySet()) {
        Map<String, Object> argsMap = (Map<String, Object>) conditionMap.get(conditionMethod);
        if (commonCondition.validate(conditionMethod, data,
            (List<Object>) argsMap.get("args")) == false) {
          exList.add(new WebConditionException((String) argsMap.get("errCode"), key));
        }
      }
    }
  }

  /**
   * フォマット検証を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-09
   * @updatedAt : 2021-06-09
   * @param key
   * @param format
   * @param sData
   * @param exList
   */
  @SuppressWarnings("unchecked")
  private void validateFormat(String key, Object formats, String sData, List<WebException> exList) {
    if (formats instanceof String) {
      if (FormatType.valueOf((String) formats) != null) {
        if (sData.matches(FORMAT_REGEX_MAP.get(formats)) == false) {
          exList.add(new WebParameterException(Luigi2Code.V0004, key));
        }
      } else {
        exList.add(new WebParameterException(Luigi2Code.V0006, key, formats));
      }
    } else if (formats instanceof List) {
      val sb = new StringBuffer();
      sb.append("^[");
      for (val format : (List<String>) formats) {
        if (FormatType.valueOf(format) != null) {
          sb.append(FORMAT_REGEX_MAP.get(format));
        } else {
          exList.add(new WebParameterException(Luigi2Code.V0006, key, formats));
        }
      }
      sb.append("]+$");
      if (sData.matches(sb.toString()) == false) {
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
  private boolean validateType(VType type, Object data) {
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
  private boolean validiateFixedList(List<String> fixedList, Object data) {

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
  private boolean validiateIntFixedList(List<Integer> fixedList, Object data) {

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
}
