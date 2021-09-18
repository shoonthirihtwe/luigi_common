package jp.co.ichain.luigi2.resources.code;

/**
 * 反社会チェックコード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeAntiSocialForceCheck {

  /**
   * 戻り値
   * 0: 該当無し
   * 4: 該当有 漢字氏名のみ一致
   * 5: 該当有 漢字氏名+住所が一致
   * 6: 該当有 漢字氏名+生年月日が一致
   * 7: 該当有 完全一致
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum Return {
    NONE("0"), ACCORD_NAME("4"), ACCORD_ALL("7");

    String val;

    Return(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
  
  /**
   * 検索方法
   * 0: 完全一致
   * 1: 漢字氏名一致
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-09-15
   * @updatedAt : 2021-09-15
   */
  public enum RetrievalMethod {
    ACCORD_ALL("0"), ACCORD_NAME("1");

    String val;

    RetrievalMethod(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
