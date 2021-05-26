package jp.co.ichain.luigi2.validity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import jp.co.ichain.luigi2.util.CollectionUtils;
import jp.co.ichain.luigi2.vo.ObjectVo;

/**
 * パラメーターを検証する
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class Validity {

  /**
   * パラメーターを検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param bidResult
   * @throws Exception
   */
  public static void validateParameter(BindingResult bidResult) throws Exception {
    if (bidResult.hasErrors()) {
      FieldError error = bidResult.getFieldError();
      String code = error.getDefaultMessage();
      String fieldName = error.getField();

      switch (code) {
        case Luigi2Code.EP002:
          WebParameterException e = new WebParameterException(code, "FD_" + fieldName);
          Class<?> cls = bidResult.getTarget().getClass();
          FormatList formatList =
              cls.getDeclaredField(error.getField()).getAnnotation(FormatList.class);
          if (formatList != null) {
            e.items = Arrays.asList(formatList.list());
          } else {
            IntFormatList intformatList =
                cls.getDeclaredField(error.getField()).getAnnotation(IntFormatList.class);
            if (intformatList != null) {
              List<Integer> intList =
                  Arrays.stream(intformatList.list()).boxed().collect(Collectors.toList());
              List<String> strList = new ArrayList<>(intList.size());
              for (Integer myInt : intList) {
                strList.add(String.valueOf(myInt));
              }
              e.items = strList;
            }
          }
          throw e;
        case Luigi2Code.EP003:
          throw new WebParameterException(code, "FD_" + fieldName,
              String.valueOf(error.getArguments()[1]));
        default:
          throw new WebParameterException(code, "FD_" + fieldName);
      }
    }
  }

  /**
   * 必須パラメーターを検証する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param paramVo
   * @param requiredFieldNames
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static void validateRequiredParameter(ObjectVo paramVo, String... requiredFieldNames)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {
    Class<?> cls = paramVo.getClass();

    List<String> items = null;
    for (String fieldName : CollectionUtils.safe(requiredFieldNames)) {
      Field field = cls.getDeclaredField(fieldName);

      field.setAccessible(true);

      Object data = field.get(paramVo);
      if (data == null || ((data instanceof String) && data.equals(""))) {
        if (items == null) {
          items = new ArrayList<String>();
        }
        items.add(fieldName);
      }
    }

    if (items != null) {
      throw new WebParameterException(Luigi2Code.EP001, null, items);
    }
  }
}
