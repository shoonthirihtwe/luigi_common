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
public class IntFormatListValidator implements ConstraintValidator<IntFormatList, Integer> {

  int[] list;

  public void initialize(IntFormatList formatList) {
    list = formatList.list();
  }

  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    if (value == null || list == null) {
      return true;
    }

    for (int item : list) {
      if (item == value) {
        return true;
      }
    }
    return false;
  }
}
