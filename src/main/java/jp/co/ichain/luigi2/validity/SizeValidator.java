package jp.co.ichain.luigi2.validity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 文字列長さ検証
 *
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class SizeValidator implements ConstraintValidator<Size, String> {

  int max;

  @Override
  public void initialize(Size size) {
    max = size.max();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.length() == 0 || max == 0) {
      return true;
    }

    if (value.length() <= max) {
      return true;
    }
    return false;
  }
}
