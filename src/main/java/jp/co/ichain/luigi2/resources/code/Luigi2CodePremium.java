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
   * 契約査定免除
   * 
   * 0:非該当 1:該当
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
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
