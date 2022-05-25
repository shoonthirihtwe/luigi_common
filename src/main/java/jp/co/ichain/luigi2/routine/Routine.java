package jp.co.ichain.luigi2.routine;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * 固有ロジック
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2022-05-24
 * @updatedAt : 2022-05-24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Routine {

}
