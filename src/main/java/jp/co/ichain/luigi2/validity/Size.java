package jp.co.ichain.luigi2.validity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import jp.co.ichain.luigi2.resources.Luigi2Code;

/**
 * 文字列長さ検証
 *
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SizeValidator.class})
public @interface Size {
  String message() default Luigi2Code.EP003;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int max() default 0;
}
