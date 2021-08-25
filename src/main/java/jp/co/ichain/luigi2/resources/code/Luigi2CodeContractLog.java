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
   * 51:保険金等支払
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ReasonGroupCode {
    DELETE("01"), TRANSFER("02"), RECIPIENT("09"), CONTRACT_ESTABLISHMENT("14"), CANCEL(
        "20"), PAYMENTMETHOD("31"), PAYMENT_OF_INSURANCE("51");

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
   * 120:経理
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ReasonCode {
    CONTRACT("010"), MAINTENANCE_REQUESTS("020"), ACCOUNTING("120");

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
   * 01:経理:保険金（給付金）支払
   * 06:成立確定
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ContactTransactionCode {
    ACCOUNTING("01"), CONTRACT_ESTABLISHMENT("06"), MAINTENANCE_REQUESTS("01"), MAINTENANCE_FIRST(
        "02"), MAINTENANCE_SECOND("03"), MAINTENANCE_CANCEL("04"), MAINTENANCE_INADEQUACY("05");

    String val;

    ContactTransactionCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * ログタイプ
   * 
   * 0:正常
   * 1:エラー
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-23
   * @updatedAt : 2021-08-23
   */
  public enum LogType {
    OK("0"), NG("1");

    String val;

    LogType(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
