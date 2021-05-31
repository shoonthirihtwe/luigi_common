package jp.co.ichain.luigi2.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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

}
