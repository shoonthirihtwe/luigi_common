package jp.co.ichain.luigi2.resources.code;

/**
 * 入金詳細コード
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeDepositDetails {

  /**
   * 明細のステータス
   *
   * S:suspence
   * M:matched
   * U:Unmatched
   * D:deleted
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum CashDetailStatus {
    SUSPENCE("S"),
    MATCHED("M"),
    UNMATCHED("U"),
    DELETED("D");

    String val;

    CashDetailStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * 引き去り結果コード
   *
   * 0:決済成功
   * 1:カード無効
   * 2:限度額オーバー
   * 3:カード残高不足
   * 4:カード有効期限範囲外
   * T:口座振替結果待ち
   * N:不明
   *
   * @author : [VJP] タン
   * @createdAt : 2021-08-27
   * @updatedAt : 2021-08-27
   */
  public enum PaymentResultCode {
    SUCCESS("0"),
    INVALID("1"),
    OVER("2"),
    INSUFFICIENT("3"),
    OUT_OF_RANGE("4"),
    TRANSFER_PENDING("T"),
    UNKNOWN("N"),
    EMPTY("E");

    String val;

    PaymentResultCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
