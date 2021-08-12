package jp.co.ichain.luigi2.resources.code;

/**
 * 証券マスタコード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeContracts {

  /**
   * 払込経路
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum PaymentMethod {
    CASH("1"), GROUP("2"), CREDIT("3"), TRANSFER("4"), CARRIER("5");

    String val;

    PaymentMethod(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
