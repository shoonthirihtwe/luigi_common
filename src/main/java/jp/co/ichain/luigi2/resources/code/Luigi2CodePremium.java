package jp.co.ichain.luigi2.resources.code;

/**
 * ステータス：保険料
 *
 * @author : [VJP] HoangNH
 * @createdAt : 2021-08-30
 * @updatedAt : 2021-08-30
 */
public class Luigi2CodePremium {

  /**
   * PremiumStatus
   *
   * M:マッチ
   * P:保留中
   *
   * @author : [VJP] HoangNH
   * @createdAt : 2021-08-30
   * @updatedAt : 2021-08-30
   */
  public enum PremiumStatus {
    CANCELED("C"),
    MATCHED("M"),
    PENDING("P"),
    REVERSED("R"),
    STOPED("S"),
    TRANSFER("T");

    String val;

    PremiumStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

}
