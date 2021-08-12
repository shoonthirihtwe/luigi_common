package jp.co.ichain.luigi2.resources.code;

/**
 * 顧客マスタコード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeCustomers {

  /**
   * 顧客の区分を管理する
   * 
   * 1:個人
   * 2:法人
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum CorporateIndividualFlag {
    INDIVIDUAL("1"), CORPORATION("2");

    String val;

    CorporateIndividualFlag(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
