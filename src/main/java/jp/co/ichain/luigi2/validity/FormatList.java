package jp.co.ichain.luigi2.validity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import jp.co.ichain.luigi2.resources.Luigi2Code;

/**
 * 指定されたフォマットパラメーター検証指定
 *
 * @author : [AOT] s.park
 * @createdAt :2021-05-25
 * @updatedAt :2021-05-25
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FormatListValidator.class})
public @interface FormatList {
  String message() default Luigi2Code.EP002;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] list() default {};
}
