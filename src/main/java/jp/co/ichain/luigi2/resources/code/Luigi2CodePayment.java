package jp.co.ichain.luigi2.resources.code;

/**
 * 支払い
 * 
 * @author : [VJP] HOANGNH
 * @createdAt : 2021-08-27
 * @updatedAt : 2021-08-27
 */
public class Luigi2CodePayment {

  /**
   * 払込方法コード
   * 
   * 戻り値
   * 
   * 3: カード 4: 口座振替
   * 
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-26
   * @updatedAt : 2021-08-26
   */
  public enum PaymentMethodCode {
    CARD("3"), ACCOUNT_TRANFER("4");

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
   * 引き去り結果コード（口座振替）
   * 
   * 戻り値
   *
   * 0（引落し成功）
   * 1（残高不足）
   * 2（預金取引なし（口座解約済等））
   * 3（預金者都合による振替停止）
   * 4（振替依頼書不備）
   * 8（委託者都合による停止）
   * 9（その他）
   * E（その他）
   * N（振替結果不明）
   * 
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-26
   * @updatedAt : 2021-08-26
   */
  public enum PaymentResultCodeForAccountTranfer {
    SUCCESSFUL_WITHDRAWAL("0"), INSUFFICIENT_FUND("1"), NO_DEPOSIT_TRANSACTION(
        "2"), SUSPENSION_TRANSFER("3"), INSUFFICIENT_TRANSFER_REQUEST(
            "4"), SUPPENSION_CONSIGNOR_CONVENIENCE(
                "8"), OTHER_FIRST("9"), OTHER_SECOND("E"), TRANSFER_RESULT_UNKNOW("N");

    String val;

    PaymentResultCodeForAccountTranfer(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }

    public static PaymentResultCodeForAccountTranfer get(String key) {
      for (PaymentResultCodeForAccountTranfer v : values()) {
        if (v.toString().equals(key)) {
          return v;
        }
      }
      return null;
    }
  }

  /**
   * 引き去り結果コード（カード）
   * 
   * 戻り値
   *
   * 0（決済成功）
   * 1（カード無効）
   * 2（限度額オーバー） 
   * 3（カード残高不足）
   * 4（カード有効期限範囲外）
   * 
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-26
   * @updatedAt : 2021-08-26
   */
  public enum PaymentResultCodeForCard {
    SUCCESSFUL_PAYMENT("0"), CARD_INVALID("1"), OVER_LIMIT("2"), INSUFFICIENT_CARD_BALANCE(
        "3"), OUTSIDE_CARD_EXPIRATION("4");

    String val;

    PaymentResultCodeForCard(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }

    public static PaymentResultCodeForCard get(String key) {
      for (PaymentResultCodeForCard v : values()) {
        if (v.toString().equals(key)) {
          return v;
        }
      }
      return null;
    }
  }

  /**
   * 戻り値 
   * 
   * M: Matched
   * U: Unmatched
   * 
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-26
   * @updatedAt : 2021-08-26
   */
  public enum CashDetailStatus {
    MATCH("M"), UNMATCH("U");

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
