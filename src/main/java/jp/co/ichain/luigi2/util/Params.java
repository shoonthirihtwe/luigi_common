package jp.co.ichain.luigi2.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NoArgsConstructor;

/**
 * Network通信値のParameter
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
@NoArgsConstructor
public class Params {
  private Map<String, String> map = new LinkedHashMap<String, String>();
  private String encoding = "UTF-8";
  final Convert convert = new Convert();

  /**
   * パラメーター追加
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param field
   * @param value
   */
  public void add(String field, String value) {
    map.put(field, value);
  }

  /**
   * パラメーター削除
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param field
   */
  public void remove(String field) {
    map.remove(field);
  }

  /**
   * パラメーター取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param field
   */
  public void get(String field) {
    map.get(field);
  }

  /**
   * Url Encodingしたパラメーター取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param field
   * @return
   */
  public String getUrlEncodingParam(String field) {
    String value = map.get(field);
    StringBuffer sbVal = new StringBuffer();
    if (value != null && value.length() > 0) {
      String[] divVal = value.split(" ");
      try {
        for (int i = 0; i < divVal.length; i++) {
          sbVal.append(URLEncoder.encode(divVal[i], encoding)).append("%20");
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      sbVal.delete(sbVal.length() - 3, sbVal.length());
    }
    return new String(sbVal);
  }

  /**
   * Url Dncodingしたパラメーター取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param field
   * @return
   */
  public String getUrlDecodingParam(String field) {
    String value = map.get(field);
    StringBuffer sbVal = new StringBuffer();
    if (value != null && value.length() > 0) {
      String[] divVal = value.split(" ");
      try {
        for (int i = 0; i < divVal.length; i++) {
          sbVal.append(URLDecoder.decode(divVal[i], encoding)).append("%20");
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      sbVal.delete(sbVal.length() - 3, sbVal.length());
    }
    return new String(sbVal);
  }


  public Params(Object obj) {
    addUsefullClassMapping(obj, false);
  }

  public Params(Object obj, boolean firstCapital) {
    addUsefullClassMapping(obj, firstCapital);
  }

  public Params(Object obj, String encoding) {
    this.encoding = encoding;
    addUsefullClassMapping(obj, false);
  }

  /**
   * Objectのフィールドをパラメーターに追加する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param obj
   * @param firstCapital
   * @return
   */
  public boolean addUsefullClassMapping(Object obj, boolean firstCapital) {
    try {
      Class<?> cls = obj.getClass();
      Field[] fields = cls.getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        int modifier = field.getModifiers();
        if (Modifier.isFinal(modifier) == false && Modifier.isStatic(modifier) == false) {
          String fieldName = field.getName();
          Object fieldValue = field.get(obj);
          if (firstCapital) {
            if (fieldName.length() > 1) {
              fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            } else {
              fieldName = fieldName.toUpperCase();
            }

          }
          if (fieldValue != null) {
            this.add(fieldName, convert.toString(fieldValue));
          }
        }
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return false;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * パラメーターを取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @return
   */
  public String getParams() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> param : map.entrySet()) {
      sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * Url Encodingしたパラメーターを取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @return
   */
  public String getUrlEncodingParams() {
    StringBuilder sb = new StringBuilder();
    try {
      String value = null;
      String[] divVal = null;
      StringBuffer sbVal = new StringBuffer();
      int i;
      for (Map.Entry<String, String> param : map.entrySet()) {
        value = param.getValue();
        sbVal.delete(0, sbVal.capacity());
        if (value != null && value.length() > 0) {
          divVal = value.split(" ");
          for (i = 0; i < divVal.length; i++) {
            sbVal.append(URLEncoder.encode(divVal[i], encoding)).append("%20");
          }
          sbVal.delete(sbVal.length() - 3, sbVal.length());
        }
        sb.append(param.getKey()).append("=").append(sbVal).append("&");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * Url Dncodingしたパラメーターを取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @return
   */
  public String getDecodingUrlParams() {
    StringBuilder sb = new StringBuilder();
    try {
      for (Map.Entry<String, String> param : map.entrySet()) {
        sb.append(param.getKey()).append("=").append(URLDecoder.decode(param.getValue(), encoding))
            .append("&");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * Encodingを取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @return
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Encodingをセットする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @return
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * パラメーターをMapで取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @return
   */
  public Map<String, String> getMap() {
    return this.map;
  }
}
