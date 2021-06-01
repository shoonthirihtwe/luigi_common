package jp.co.ichain.luigi2.validity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.validation.constraints.Size;
import jp.co.ichain.luigi2.exception.WebListException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import jp.co.ichain.luigi2.util.ClassUtils;
import jp.co.ichain.luigi2.vo.ObjectVo;
import lombok.val;

/**
 * パラメーターを検証する
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class Validity {

  private final static String VALIDITY_EMAIL =
      "^[0-9a-zA-Z]([-_\\\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\\\.]?[0-9a-zA-Z])*\\\\.[a-zA-Z]{2,3}(.[a-zA-Z]{2,3})?$";
  private final static String VALIDITY_TEL = "^\\\\d{3,4}-?\\\\d{3,4}-?\\\\d{4}$";

  /**
   * パラメーターを検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-01
   * @updatedAt : 2021-06-01
   * @param voList
   * @param requiredFieldNames
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws WebListException
   */
  public static void validateParameter(ObjectVo vo, String... requiredFieldNames)
      throws WebListException, IllegalArgumentException, IllegalAccessException {
    validateParameter(new ObjectVo[] {vo}, requiredFieldNames);
  }

  /**
   * パラメーターを検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param bidResult
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws Exception
   */
  public static void validateParameter(ObjectVo[] voList, String... requiredFieldNames)
      throws WebListException, IllegalArgumentException, IllegalAccessException {
    WebListException result = null;
    for (int i = 0; i < voList.length; i++) {
      Class<?> cls = voList[i].getClass();
      for (Field field : ClassUtils.getUsefullFields(cls)) {
        String fieldName = field.getName();
        VoFieldInfo fieldInfo = field.getAnnotation(VoFieldInfo.class);
        String fieldDesName = fieldInfo != null ? fieldInfo.name() : field.getName();

        // required Validate
        Map<String, Boolean> requiredFieldNamesMap =
            Arrays.asList(requiredFieldNames).stream().collect(Collectors.toMap(f -> f, f -> true));
        if (requiredFieldNamesMap.get(fieldName)) {
          field.setAccessible(true);
          Object data = field.get(voList[i]);
          if (data == null || ((data instanceof String) && data.equals(""))) {
            if (result == null) {
              result = new WebListException();
            }
            result.addWebException(new WebParameterException(Luigi2Code.P001_V0001, fieldDesName));
          } else if (fieldInfo != null) {
            // Validate
            for (val validity : fieldInfo.validitys()) {
              if (VALID_MAP.get(validity).apply(data, field)) {
                result.addWebException(new WebParameterException(validity.name(), fieldDesName));
              }
            }
          }
        }
      }
    }
    if (result != null) {
      throw result;
    }
  }

  public static Map<VoFieldInfo.Validity, BiFunction<Object, Field, Boolean>> VALID_MAP =
      new HashMap<VoFieldInfo.Validity, BiFunction<Object, Field, Boolean>>();
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
    VALID_MAP.put(VoFieldInfo.Validity.Email, (obj, field) -> {
      String value = (String) obj;
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
    VALID_MAP.put(VoFieldInfo.Validity.Tel, (obj, field) -> {
      String value = (String) obj;
      if (value == null || value.length() == 0) {
        return true;
      }

      return value.matches(VALIDITY_TEL);
    });

    /**
     * 文字列フォーマット検証
     * 
     * @author : [AOT] s.paku
     * @createdAt : 2021-05-31
     * @updatedAt : 2021-05-31
     * @param value
     * @param field
     * @return
     */
    VALID_MAP.put(VoFieldInfo.Validity.Format, (obj, field) -> {
      String value = (String) obj;
      FormatList fieldInfo = field.getAnnotation(FormatList.class);
      if (value == null || value.length() == 0 || fieldInfo == null) {
        return true;
      }

      for (String item : fieldInfo.list()) {
        if (item.equals(value)) {
          return true;
        }
      }
      return false;
    });

    /**
     * 数字フォーマット検証
     * 
     * @author : [AOT] s.paku
     * @createdAt : 2021-05-31
     * @updatedAt : 2021-05-31
     * @param value
     * @param field
     * @return
     */
    VALID_MAP.put(VoFieldInfo.Validity.IntFormat, (obj, field) -> {
      Integer value = (Integer) obj;
      IntFormatList fieldInfo = field.getAnnotation(IntFormatList.class);

      if (value == null || fieldInfo == null) {
        return true;
      }

      for (int item : fieldInfo.list()) {
        if (item == value) {
          return true;
        }
      }
      return false;
    });

    /**
     * サイズフォーマット検証
     * 
     * @author : [AOT] s.paku
     * @createdAt : 2021-05-31
     * @updatedAt : 2021-05-31
     * @param value
     * @param field
     * @return
     */
    VALID_MAP.put(VoFieldInfo.Validity.Size, (obj, field) -> {
      String value = (String) obj;
      Size fieldInfo = field.getAnnotation(Size.class);

      if (value == null || fieldInfo.max() == 0) {
        return true;
      }

      if (value.length() <= fieldInfo.max() && value.length() >= fieldInfo.min()) {
        return true;
      }
      return false;
    });

    /**
     * サイズフォーマット検証
     * 
     * @author : [AOT] s.paku
     * @createdAt : 2021-05-31
     * @updatedAt : 2021-05-31
     * @param value
     * @param field
     * @return
     */
    VALID_MAP.put(VoFieldInfo.Validity.ByteSize, (obj, field) -> {
      String value = (String) obj;
      Size fieldInfo = field.getAnnotation(Size.class);

      if (value == null || fieldInfo.max() == 0) {
        return true;
      }

      if (value.length() <= fieldInfo.max() && value.getBytes().length >= fieldInfo.min()) {
        return true;
      }
      return false;
    });
  }
}
