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
   * 01:1次査定待
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
    APPLYING_COMPLETE("00"), WAITING_FOR_FIRST_ASSESSMENT("01"), WAITING_FOR_SECOND_ASSESSMENT("02"), WAITING_FOR_FIRST_PAYMENT("03"), CHECK_FOR_PAYMENT_METHOD(
        "05"), INADEQUACY("15"), WITHDRAWAL("21"), ESTABLISHMENT("31");

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

  /**
   * 一次査定結果
   *
   * 00:通常引受
   * 01:特別条件付引受
   * 02:謝絶
   * 03:不備
   * 04:取下
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum FirstAssessmentResults {
    COMPLATE("00"), CANCEL("01"), INADEQUACY("02"), UNREADY("03"), WITHDRAWAL("04");

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
   * 2次査定結果
   * 
   * 00:承認
   * 01:差戻
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-09-13
   * @updatedAt : 2021-09-13
   */
  public enum SecondAssessmentResults {
    APPROVAL("00"), DISAPPROVAL("01");

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
