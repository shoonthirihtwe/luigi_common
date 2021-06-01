package jp.co.ichain.luigi2.validity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jp.co.ichain.luigi2.resources.Luigi2Code;

/**
 * Vo フィルード情報
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VoFieldInfo {

  /**
   * フィルード名
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   */
  public String name()

  default "";

  /**
   * 検証項目
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   */
  public Validity[] validitys() default {};


  public enum Validity {
    Email(Luigi2Code.P003_V0001), Tel(Luigi2Code.P003_V0001), Format(
        Luigi2Code.P005_V0001), IntFormat(
            Luigi2Code.P005_V0001), Size(Luigi2Code.P002_V0001), ByteSize(Luigi2Code.P002_V0001);

    private final String name;

    private Validity(String s) {
      name = s;
    }

    public boolean equalsName(String otherName) {
      return name.equals(otherName);
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
