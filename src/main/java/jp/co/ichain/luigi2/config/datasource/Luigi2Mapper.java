package jp.co.ichain.luigi2.config.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * mew2userDBへのMapperAnnotation
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-02-10
 * @updatedAt : 2021-04-14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Luigi2Mapper {
  String value() default "";
}
