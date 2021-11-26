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

  /**
   * 基本給付額単位
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-11-26
   * @updatedAt : 2021-11-26
   */
  public enum BenefitBaseUnit {
    F("F"), E("E"), A("A");

    String val;

    BenefitBaseUnit(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
