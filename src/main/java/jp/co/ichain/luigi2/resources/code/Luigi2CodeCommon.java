package jp.co.ichain.luigi2.resources.code;

/**
 * 共通コード
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeCommon {

  /**
   * フラグ
   * 
   * 0:FALSE
   * 1:TRUE
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum FlagCode {
    FALSE("0"), TRUE("1");

    String val;

    FlagCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

}
