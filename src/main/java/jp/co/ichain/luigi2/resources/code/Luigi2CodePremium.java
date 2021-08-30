package jp.co.ichain.luigi2.resources.code;

/**
 * 販売プラン：保険種類
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
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
