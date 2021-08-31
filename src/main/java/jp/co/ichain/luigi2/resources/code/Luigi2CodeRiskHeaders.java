package jp.co.ichain.luigi2.resources.code;

/**
 * 保障内容
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-08-25
 * @updatedAt : 2021-08-25
 */
public class Luigi2CodeRiskHeaders {

  /**
   * 契約のステータス
   * 
   * 40:有効中
   * 62:更新
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-25
   * @updatedAt : 2021-08-25
   */
  public enum ProductStatus {
    ENABLED("40"), RENEWAL("62");

    String val;

    ProductStatus(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
