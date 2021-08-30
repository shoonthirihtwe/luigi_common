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
   * 
   * @author : [VJP] HoangNH
   * @createdAt : 2021-08-30
   * @updatedAt : 2021-08-30
   */
  public enum PremiumStatus {
    MATCHED("M");

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
