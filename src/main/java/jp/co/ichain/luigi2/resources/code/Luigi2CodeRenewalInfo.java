package jp.co.ichain.luigi2.resources.code;

/**
 * 更新用トランザクション
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-08-30
 * @updatedAt : 2021-08-30
 */
public class Luigi2CodeRenewalInfo {

  /**
   * 更新タイプ
   * 
   * E:満了
   * R:更新
   * V:契約内容を変更して更新
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-30
   * @updatedAt : 2021-08-30
   */
  public enum RenewalType {
    EXPIRATION("E"), RENEWAL("R"), MODIFY("V");

    String val;

    RenewalType(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
