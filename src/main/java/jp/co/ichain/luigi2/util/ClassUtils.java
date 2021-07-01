package jp.co.ichain.luigi2.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クレス操作 Utils
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
public class ClassUtils {


  /**
   * 可用フィルード取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param cls
   * @return
   */
  public static List<Field> getUsefullFields(Class<?> cls) {
    List<Field> arrFields = new ArrayList<Field>();
    Field[] fields = cls.getDeclaredFields();

    for (Field field : fields) {
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier) == false && Modifier.isStatic(modifier) == false) {
        arrFields.add(field);
      }
    }
    return arrFields;
  }

  /**
   * MapにObjectのフィールド：値を入れる
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param map
   * @param object
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws ParseException
   */
  public static void setFieldValueAndConvert(Map<String, String> map, Object object)
      throws IllegalArgumentException, IllegalAccessException, ParseException {
    Class<?> cls = object.getClass();
    Field[] fields = cls.getDeclaredFields();

    Convert convert = new Convert();
    for (Field field : fields) {
      field.setAccessible(true);
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier)) {
        continue;
      }

      String key = field.getName();
      field.set(object, convert.toType(field.getType(), map.get(key)));
    }
  }

  /**
   * VoをMapに変換
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param object
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static Map<String, Object> getUseFullFieldValue(Object object)
      throws IllegalArgumentException, IllegalAccessException {
    Map<String, Object> map = new HashMap<String, Object>();
    Class<?> cls = object.getClass();
    Field[] fields = cls.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier))
        continue;

      String key = field.getName();
      map.put(key, field.get(object));
    }

    return map;
  }
}
