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
    SUSPENCE("S"), MATCHED("M"), UNMATCHED("U"), DELETED("D");

    String val;

    CashDetailStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

}
