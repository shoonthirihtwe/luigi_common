package jp.co.ichain.luigi2.resources.code;

/**
 * 契約ログコード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeContractLog {

  /**
   * 事由種別（大分類）
   * 
   * 01:契約取消／契約解除
   * 02:名義住所変更
   * 09:受取人変更
   * 14:契約成立
   * 20:解約
   * 31:払込経路変更
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ReasonGroupCode {
    DELETE("01"), TRANSFER("02"), RECIPIENT("09"), CONTRACT_ESTABLISHMENT("14"), CANCEL("20"), PAYMENTMETHOD("31");

    String val;

    ReasonGroupCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * 事由種別（大分類）
   * 
   * 010:新契
   * 020:保全
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ReasonCode {
    CONTRACT("010"), MAINTENANCE_REQUESTS("020");

    String val;

    ReasonCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
  
  /**
   * 異動コード
   * 
   * 06:成立確定
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ContactTransactionCode {
    CONTRACT_ESTABLISHMENT("06");

    String val;

    ContactTransactionCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
