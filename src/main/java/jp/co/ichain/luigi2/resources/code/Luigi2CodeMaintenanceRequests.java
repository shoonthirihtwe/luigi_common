package jp.co.ichain.luigi2.resources.code;

/**
 * 保全申請コード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeMaintenanceRequests {

  /**
   * 保全申請ステータス
   * 
   * 0: 受付済
   * 1: 一次査定済
   * 2: 二次査定済
   * A: 承諾・適用待ち
   * U: 適用済
   * D: 不承諾
   * R: 却下
   * C: 取下げ
   * I:不備
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum RequestStatus {
    RECEIPT("0"), FIRST("1"), SECOND("2"), AGREEMENT("A"), APPLY("U"), NONAGREEMENT("D"), REJECTION(
        "R"), CANCEL("C"), INADEQUACY("I");

    String val;

    RequestStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * 保全申請分類
   * 
   * 11: 名義住所変更
   * 14: 受取人の変更
   * 31: 払込経路変更
   * 41: 解約 〔解約・失効〕
   * 42: 解除
   * 44: 取消
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum TransactionCode {
    TRANSFER("11"), RECIPIENT("14"), PAYMENTMETHOD("31"), CANCEL("41"), DELETE("42"), RETRACTION(
        "44");

    String val;

    TransactionCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }

    public static TransactionCode get(String key) {
      for (TransactionCode v : values()) {
        if (v.toString().equals(key)) {
          return v;
        }
      }
      return null;
    }
  }

  /**
   * 一次査定結果
   * 
   * 00:通常引受
   * 01:取下
   * 02:不備
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum FirstAssessmentResults {
    COMPLATE("00"), CANCEL("01"), INADEQUACY("02");

    String val;

    FirstAssessmentResults(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * 二次査定結果
   * 
   * 00:承認
   * 01:差戻
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum SecondAssessmentResults {
    COMPLATE("00"), REJECT("01");

    String val;

    SecondAssessmentResults(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
