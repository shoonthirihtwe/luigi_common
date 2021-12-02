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
   * 4:申請ステータス：5 かつ査定ステータス：3 
   * 5:申請ステータス：5 かつ査定ステータス：2 or 5(無査定支払) 
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
   * Claim 検索ステータス 
   * 0:受付済 
   * 1:１次査定中 
   * 2:２次査定中 
   * 3:2次査定済
   * 4:支払承認済
   * 5:支払処理済
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-12-01
   * @updatedAt : 2021-12-01
   */
  public enum SearchClaimStatus {
    RECEPTION_COMPLETED("0"), FIRST_ASSESSMENT("1"), SECOND_ASSESSMENT(
        "2"), SECOND_ASSESSMENT_COMPLETED("3"), PAYMENT_APPROVAL_COMPLETED("4"), PAYMENT_PROCESSED("5");

    String val;

    SearchClaimStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
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
