package jp.co.ichain.luigi2.resources.code;

/**
 * 入金
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-08-27
 * @updatedAt : 2021-08-27
 */
public class Luigi2CodeDepositHeaders {
  /**
   * 払込方法コード
   * 
   * 1:現金
   * 3:カード
   * 4:口座振替
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-27
   * @updatedAt : 2021-08-27
   */
  public enum PaymentMethodCode {
    CASH("1"),
    CARD("3"),
    BANK("4");

    String val;

    PaymentMethodCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * ステータス
   * 
   * A:入力完了：マッチング待ち
   * D:DELETED
   * P:PROCESSED
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-27
   * @updatedAt : 2021-08-27
   */
  public enum BatchStatus {
    WAITING("A"),
    DELETED("D"),
    PROCESSED("P"),
    TRANSFER("T");

    String val;

    BatchStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * 収集ルート
   * 
   * R:レギュラー
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-27
   * @updatedAt : 2021-08-27
   */
  public enum CollectionRoute {
    REGULAR("R");

    String val;

    CollectionRoute(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
