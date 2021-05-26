package jp.co.ichain.luigi2.validity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 指定されたフォマットパラメーター検証する
 *
 * @author : [AOT] s.park
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class FormatListValidator implements ConstraintValidator<FormatList, String> {

  String[] list;

  public void initialize(FormatList formatList) {
    list = formatList.list();
  }

  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.length() == 0 || list == null) {
      return true;
    }

    for (String item : list) {
      if (item.equals(value)) {
        return true;
      }
    }
    return false;
  }
}
