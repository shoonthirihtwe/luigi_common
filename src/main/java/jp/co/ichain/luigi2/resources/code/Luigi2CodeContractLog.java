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
   * 05:保険料請求
   * 09:受取人変更
   * 14:契約成立
   * 20:解約
   * 24:自動更新
   * 28:契約満了
   * 31:払込経路変更
   * 39:失効
   * 51:保険金等支払
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ReasonGroupCode {
    DELETE("01"), TRANSFER("02"), INSURANCE_PREMIUM_CLAIM("05"), RECIPIENT(
        "09"), CONTRACT_ESTABLISHMENT("14"), CANCEL("20"), AUTOMATIC_UPDATING(
            "24"), CONTRACT_EXPIRATION(
                "28"), PAYMENTMETHOD("31"), EXPRIED("39"), PAYMENT_OF_INSURANCE("51");

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
   * 030:請収
   * 050:更新
   * 120:経理
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ReasonCode {
    CONTRACT("010"), MAINTENANCE_REQUESTS("020"), RECEIPT("030"), RENEWAL("050"), ACCOUNTING("120");

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
   * 11:保険料収納 
   * 21:保険料未納
   * 24:契約満了 更新
   * 25:契約満了 保険料変更
   * 28:契約満了 満了
   * 90:失効
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ContactTransactionCode {
    ACCOUNTING("01"), CONTRACT_ESTABLISHMENT("06"), MAINTENANCE_REQUESTS("01"), MAINTENANCE_FIRST(
        "02"), MAINTENANCE_SECOND("03"), MAINTENANCE_RETURN("04"), MAINTENANCE_INADEQUACY(
            "05"), MAINTENANCE_CANCEL("06"), INSURANCE_PREMIUM_STORAGE(
                "11"), NON_INSURANCE_PREMIUM_STORAGE("21"), CONTRACT_RENEWAL(
                    "24"), INSURANCE_PREMIUM_CHANGE("25"), CONTRACT_EXPIRATION("28"), EXPRIED("90");

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
