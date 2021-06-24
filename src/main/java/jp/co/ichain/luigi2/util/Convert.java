package jp.co.ichain.luigi2.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文字列を特定のタイプにコンバート
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
public class Convert {

  private SimpleDateFormat format;
  private String nullDateFormat;

  public Convert() {
    this.format = new SimpleDateFormat("yyyy-MM-dd");
  }

  /**
   * @author : [AOT] s.park
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   */
  public Convert(String format) {
    this.format = new SimpleDateFormat(format);
  }

  /**
   * 文字列に変換
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param value
   * @return
   */
  public String toString(Object value) {
    if (value == null) {
      return null;
    }
    var result = "";
    if (value instanceof Date && format != null) {
      result = format.format((Date) value);
    } else {
      result = String.valueOf(value);
    }
    return result;
  }

  /**
   * 文字列を該当するタイプに変換
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param type
   * @param value
   * @return
   * @throws ParseException
   */
  public Object toType(Type type, String value) throws ParseException {
    if (type == null || value == null) {
      return null;
    }

    Object result = null;

    // String Format
    if (type.equals(String.class)) {
      return value;
    }
    // Boolean Format
    if (type.equals(Boolean.class) || type.equals(boolean.class)) {
      return Boolean.parseBoolean(value);
    }

    try {
      // Date Format
      if (type.equals(Date.class)) {
        if (nullDateFormat.equals(value) == false) {
          result = format.parse(value);
        } else {
          return null;
        }
      } else if (type.equals(Byte.class)) { // Number Format
        result = Byte.parseByte(value);
      } else if (type.equals(Short.class)) {
        result = Short.parseShort(value);
      } else if (type.equals(Integer.class)) {
        result = (int) Float.parseFloat(value);
      } else if (type.equals(Long.class)) {
        result = (long) Double.parseDouble(value);
      } else if (type.equals(Float.class)) {
        result = Float.parseFloat(value);
      } else if (type.equals(Double.class)) {
        result = Double.parseDouble(value);
      } else if (type.equals(BigDecimal.class)) {
        result = new BigDecimal(value);
      }

    } catch (NumberFormatException e) {
      result = null;
    }

    // 変換したら返す
    if (result != null) {
      return result;
    }

    // Number Format
    if (type.equals(byte.class)) {
      try {
        result = Byte.parseByte(value);
      } catch (NumberFormatException e) {
        result = (byte) 0;
      }
    } else if (type.equals(short.class)) {
      try {
        result = Short.parseShort(value);
      } catch (NumberFormatException e) {
        result = (short) 0;
      }
    } else if (type.equals(int.class)) {
      try {
        result = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        result = 0;
      }
    } else if (type.equals(long.class)) {
      try {
        result = Long.parseLong(value);
      } catch (NumberFormatException e) {
        result = 0L;
      }
    } else if (type.equals(float.class)) {
      try {
        result = Float.parseFloat(value);
      } catch (NumberFormatException e) {
        result = 0.0f;
      }
    } else if (type.equals(double.class)) {
      try {
        result = Double.parseDouble(value);
      } catch (NumberFormatException e) {
        result = 0.0;
      }
    }

    return result;
  }

  /**
   * 基本系に変換
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param type
   * @return
   */
  public static Class<?> toPrimitiveType(Class<?> type) {
    if (type == Integer.class) {
      return int.class;
    } else if (type.equals(Long.class)) {
      return long.class;
    } else if (type.equals(Boolean.class)) {
      return boolean.class;
    } else if (type.equals(Float.class)) {
      return float.class;
    } else if (type.equals(Double.class)) {
      return double.class;
    } else if (type.equals(Date.class)) {
      return long.class;
    }
    return type;
  }
}
