package jp.co.ichain.luigi2.validity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import jp.co.ichain.luigi2.resources.Luigi2Code;

/**
 * 電話番号検証
 *
 * @author : [AOT] s.park
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "^\\d{3,4}-?\\d{3,4}-?\\d{4}$", message = Luigi2Code.P003_V0001)
public @interface Tel {
  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
