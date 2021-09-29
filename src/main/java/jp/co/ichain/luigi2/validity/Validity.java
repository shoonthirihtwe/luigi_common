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
import jp.co.ichain.luigi2.exception.WebConversionException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.CodeMasterResources;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
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
  ConditionUtils commonCondition;

  @Autowired
  ConversionUtils conversionUtils;

  @Autowired
  CodeMasterResources codeMasterResources;

  /**
   * パラメータータイプ
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   */
  enum Vtype {
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
    EMAIL, TEL, HIRA, NUM, ZENNUM, KANA, HANKANA, KANJI, ALPHA, ZIPCODE, HYPHEN_SPACE
  }

  private final Map<String, String> formatRegexMap;

  {
    formatRegexMap = new HashMap<String, String>();
    formatRegexMap.put("EMAIL",
        "^[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+(\\.[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+)*@[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+(\\.[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^$\\-\\|]+)*$");
    formatRegexMap.put("TEL", "^\\d{3,4}-?\\d{3,4}-?\\d{4}$");
    formatRegexMap.put("ZIPCODE", "^\\d{3}-?\\d{4}$");
    formatRegexMap.put("HIRA", "\\u3040-\\u309F");
    formatRegexMap.put("NUM", "0-9");
    formatRegexMap.put("ZENNUM", "０-９");
    formatRegexMap.put("KANA", "\\u30a0-\\u30ff");
    formatRegexMap.put("HANKANA", "\\uFF65-\\uFF9F");
    formatRegexMap.put("KANJI", "\\u4E00-\\u9FFF々ヶ");
    formatRegexMap.put("ALPHA", "a-zA-Z");
    formatRegexMap.put("HYPHEN_SPACE", "ー－‐\\- 　");
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
      Integer tenantId, Map<String, Object> paramMap, List<WebException> exList)
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
        val type = Vtype.valueOf(validityVo.getType());

        // Required
        if (validityVo.getRequired() && data == null) {
          exList.add(new WebParameterException(Luigi2ErrorCode.V0001, key));
        }

        // type validate
        if (type != Vtype.OBJECT) {
          exList.add(new WebParameterException(Luigi2ErrorCode.V0005, key));
        } else if (data != null) {

          // condition
          validateCondition(validityVo, key, data, tenantId, exList);

          // Object recursive call
          if (validityVo.getArray()) {
            if (data instanceof List) {
              List<Map<String, Object>> list = (List<Map<String, Object>>) data;
              for (val map : list) {
                validate(validityMap, objValidityMap, tenantId, map, exList);
              }
            } else {
              exList.add(new WebParameterException(Luigi2ErrorCode.V0005, key));
            }
          } else {
            if (data instanceof Map) {
              validate(validityMap, objValidityMap, tenantId, (Map<String, Object>) data, exList);
            } else {
              exList.add(new WebParameterException(Luigi2ErrorCode.V0005, key));
            }
          }
        }
      } else if ("param-key".equals(key) == false) {
        val validityVo = validityMap.get(validity);
        // type
        val type = Vtype.valueOf(validityVo.getType());

        if (validityVo.getArray()) {
          // Condition
          if (validityVo.getIsChildrenCondition() == false) {
            validateCondition(validityVo, key, data, tenantId, exList);
          }
          if (data instanceof List) {
            if (Vtype.valueOf(validityVo.getType()) != Vtype.OBJECT) {
              List<Object> list = (List<Object>) data;
              for (val item : list) {
                validate(validityVo, serviceInstanceMap, exList, key, item, tenantId,
                    validityVo.getIsChildrenCondition());
              }
            } else {
              List<Map<String, Object>> list = (List<Map<String, Object>>) data;
              for (val map : list) {
                validate(validityVo, serviceInstanceMap, exList, key, map, tenantId,
                    validityVo.getIsChildrenCondition());
              }
            }

          } else if (data != null) {
            exList.add(new WebParameterException(Luigi2ErrorCode.V0005, key));
          }
        } else {
          var vdata = validateType(type, data, exList);
          if (data != null && vdata == null) {
            exList.add(new WebParameterException(Luigi2ErrorCode.V0005, key));
          }
          vdata = convert(validityVo, key, vdata, tenantId, exList);

          paramMap.put(key, vdata);
          validate(validityVo, serviceInstanceMap, exList, key, vdata, tenantId, true);
        }
      }
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
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  private void validate(ValidityVo validityVo, Map<String, Object> serviceInstanceMap,
      List<WebException> exList, String key, Object data, Integer tenantId,
      boolean isChildrenCondition)
      throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // Required
    if (validityVo.getRequired() && data == null) {
      exList.add(new WebParameterException(Luigi2ErrorCode.V0001, key));
    }

    if (data != null) {
      // Condition
      if (isChildrenCondition) {
        validateCondition(validityVo, key, data, tenantId, exList);
      }
      // type is string
      if (Vtype.STRING.toString().equals(validityVo.getType())) {
        String strData = (String) data;

        // min max
        if (validityVo.getMin() != null || validityVo.getMax() != null) {
          int length = 0;
          if (validityVo.getIsBinaryLength()) {
            length = strData.getBytes("UTF-8").length;
          } else {
            length = strData.length();
          }
          // min
          if (validityVo.getMin() != null && validityVo.getMin() > length) {
            exList.add(new WebParameterException(Luigi2ErrorCode.V0002, key, validityVo.getMin()));
          }
          // max
          if (validityVo.getMax() != null && validityVo.getMax() < length) {
            exList.add(new WebParameterException(Luigi2ErrorCode.V0003, key, validityVo.getMax()));
          }
        }

        // formats
        if (validityVo.getFormats() != null) {
          validateFormat(key, validityVo.getFormats(), strData, exList);
        }

        // regex
        if (validityVo.getRegex() != null) {
          validateRegex(key, validityVo.getRegex(), strData, exList);
        }
      }

      // codeMaster
      if (validiateCodeMaster(tenantId, validityVo.getCodeMaster(), data) == false) {
        exList.add(new WebParameterException(Luigi2ErrorCode.V0009, key));
      }

      // fixed
      if (validiateFixedList(validityVo.getFixedList(), data) == false) {
        exList.add(new WebParameterException(Luigi2ErrorCode.V0004, key));
      }
      if (validiateIntFixedList(validityVo.getIntFixedList(), data) == false) {
        exList.add(new WebParameterException(Luigi2ErrorCode.V0004, key));
      }
    }
  }

  /**
   * 自動変換する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @param validityVo
   * @param key
   * @param data
   * @param tenantId
   * @param exList
   * @return
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public Object convert(ValidityVo validityVo, String key, Object data, Integer tenantId,
      List<WebException> exList)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (validityVo.getConversion() != null && data != null) {
      val conditionMethod = validityVo.getConversion();
      try {
        data = conversionUtils.convert(conditionMethod, tenantId, data);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof WebException) {
          exList.add((WebException) e.getCause());
        } else {
          exList.add(new WebConversionException(Luigi2ErrorCode.V0006, key));
        }
      }
    }
    return data;
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
  public void validateCondition(ValidityVo validityVo, String key, Object data, Integer tenantId,
      List<WebException> exList)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    // Common Condition
    if (validityVo.getCondition() != null) {
      val conditionMap = validityVo.getCondition();

      for (String conditionMethod : conditionMap.keySet()) {
        Map<String, Object> argsMap = (Map<String, Object>) conditionMap.get(conditionMethod);
        try {
          if (commonCondition.validate(conditionMethod, tenantId, data,
              (List<Object>) argsMap.get("args")) == false) {
            val errArgs = (List<Object>) argsMap.get("errArgs");
            if (errArgs != null && errArgs.size() > 0) {
              exList.add(new WebConditionException((String) argsMap.get("errCode"), errArgs));
            } else {
              exList.add(new WebConditionException((String) argsMap.get("errCode"), key));
            }
          }
        } catch (Exception e) {
          val errArgs = (List<Object>) argsMap.get("errArgs");
          if (errArgs != null && errArgs.size() > 0) {
            exList.add(new WebConditionException((String) argsMap.get("errCode"), errArgs));
          } else {
            exList.add(new WebConditionException((String) argsMap.get("errCode"), key));
          }
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
   * @param strData
   * @param exList
   */
  @SuppressWarnings("unchecked")
  private void validateFormat(String key, Object formats, String strData,
      List<WebException> exList) {
    if (formats instanceof String) {
      if (FormatType.valueOf((String) formats) != null) {
        if (strData.matches(formatRegexMap.get(formats)) == false) {
          exList.add(new WebParameterException(Luigi2ErrorCode.V0004, key));
        }
      } else {
        exList.add(new WebParameterException(Luigi2ErrorCode.V0006, key, formats));
      }
    } else if (formats instanceof List) {
      val sb = new StringBuffer();
      sb.append("^[");
      for (val format : (List<String>) formats) {
        if (FormatType.valueOf(format) != null) {
          sb.append(formatRegexMap.get(format));
        } else {
          exList.add(new WebParameterException(Luigi2ErrorCode.V0006, key, formats));
        }
      }
      sb.append("]+$");
      if (strData.matches(sb.toString()) == false) {
        exList.add(new WebParameterException(Luigi2ErrorCode.V0004, key));
      }
    }
  }

  /**
   * 正規規則検証を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-25
   * @updatedAt : 2021-08-25
   * @param key
   * @param regex
   * @param strData
   * @param exList
   */
  private void validateRegex(String key, String regex, String strData,
      List<WebException> exList) {
    if (strData.matches(regex) == false) {
      exList.add(new WebParameterException(Luigi2ErrorCode.V0004, key));
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
  private Object validateType(Vtype type, Object data, List<WebException> exList) {
    switch (type) {
      case STRING:
        if (data instanceof String) {
          return data;
        }
        return null;
      case BOOL:
        if (data instanceof Boolean) {
          return data;
        } else if (data instanceof String) {
          try {
            return Boolean.valueOf((String) data);
          } catch (Exception e) {
            return null;
          }
        }
        return null;
      case DATE:
      case INT:
        if (data instanceof Integer || data instanceof Long || data instanceof Short
            || data instanceof Byte || data instanceof BigInteger) {
          return data;
        } else if (data instanceof String) {
          try {
            return Long.valueOf((String) data);
          } catch (Exception e) {
            return null;
          }
        }
        return null;
      case FRACTION:
        if (data instanceof Double || data instanceof Float || data instanceof BigDecimal) {
          return data;
        } else if (data instanceof String) {
          try {
            return Long.valueOf((String) data);
          } catch (Exception e) {
            return null;
          }
        }
        return null;
      default:
        return data;
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
   * コードマスターで検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-07
   * @updatedAt : 2021-07-07
   * @param tenantId
   * @param codeMasterKey
   * @param data
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  private boolean validiateCodeMaster(Integer tenantId, String codeMasterKey, Object data)
      throws JsonMappingException, JsonProcessingException {

    if (codeMasterKey == null || data == null) {
      return true;
    }
    val fixedList = codeMasterResources.get(tenantId).get(codeMasterKey);
    if (fixedList == null || fixedList.size() == 0) {
      return true;
    }

    for (val item : fixedList) {
      if (data.equals(item.getCodeValue())) {
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
