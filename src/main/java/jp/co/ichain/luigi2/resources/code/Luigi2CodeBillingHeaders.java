package jp.co.ichain.luigi2.resources.code;

/**
 * 請求コード
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeBillingHeaders {

  /**
   * 請求ヘッダー状態コード
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum BillingHeaderStatus {
    BILLED("B"), CASH_BATCH_CREATED("C"), DATA_CREATED("D"), MONEY_RECEIVED("R");

    String val;

    BillingHeaderStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
