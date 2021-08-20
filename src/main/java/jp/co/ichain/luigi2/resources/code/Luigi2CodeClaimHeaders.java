package jp.co.ichain.luigi2.resources.code;

/**
 * 保険金・給付金請求情報
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-08-20
 * @updatedAt : 2021-08-20
 */
public class Luigi2CodeClaimHeaders {

  /**
   * 申請ステータス
   *
   * 2:１次査定中
   * 3:２次査定中
   * 4:差戻中
   * 5:査定完了
   * 6:支払処理済
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-20
   * @updatedAt : 2021-08-20
   */
  public enum ClaimStatus {
    FIRST_ASSESSMENT("2"), SECOND_ASSESSMENT("3"), REJECT("4"), COMPLETED("5"), PAYMENT_PROCESSED(
        "6");

    String val;

    ClaimStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
    
    public static ClaimStatus get(String key) {
      for (ClaimStatus v : values()) {
        if (v.toString().equals(key)) {
          return v;
        }
      }
      return null;
    }
  }

  /**
   * 査定ステータス
   *
   * 1:査定前
   * 2:承諾
   * 3:不承諾
   * 4:不備
   * 5:無査定支払
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-20
   * @updatedAt : 2021-08-20
   */
  public enum UnderwritingStatus {
    BEFORE_ASSESSMENT("1"), CONSENT("2"), DISSENT("3"), DEFECT("4"), UNASSESSED_PAYMENT("5");

    String val;

    UnderwritingStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }

    public static UnderwritingStatus get(String key) {
      for (UnderwritingStatus v : values()) {
        if (v.toString().equals(key)) {
          return v;
        }
      }
      return null;
    }
  }

}
