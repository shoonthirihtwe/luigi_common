package jp.co.ichain.luigi2.resources.code;

/**
 * 入金詳細コード
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeUnderwritings {

  /**
   * 明細のステータス
   * 
   * 00:受付済
   * 02:1次査済
   * 03:初回保険料決済待
   * 04:条件付契約査定中
   * 05:決済方法確認待
   * 15:謝絶
   * 20:取消
   * 21:取下
   * 31:成立確定
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ContractStatus {
    WAITING_FOR_PAYMENT("01"), CHECK_FOR_PAYMENT_METHOD("05"), ESTABLISHMENT("31");

    String val;

    ContractStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
  
  /**
   * 反社会的勢力チェック
   * 
   * 0:非該当
   * 1:該当
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum AntisocialForcesCheck {
    NOT_APPLICABLE("0"), APPLICABLE("1");

    String val;

    AntisocialForcesCheck(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

}
