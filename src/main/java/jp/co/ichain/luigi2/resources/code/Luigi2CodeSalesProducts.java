package jp.co.ichain.luigi2.resources.code;

/**
 * 販売プラン：保険種類
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeSalesProducts {

  /**
   * 契約査定免除
   * 
   * 0:非該当 1:該当
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ContractUnderwritingWaivers {
    MATCHED("0"), UNMATCHED("1");

    String val;

    ContractUnderwritingWaivers(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  public enum RoundingType {
    ROUNDUP("1"), ROUNDDOWN("2"), ROUND("3");

    String val;

    RoundingType(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }

    public static RoundingType get(String key) {
      for (RoundingType v : values()) {
        if (v.toString().equals(key)) {
          return v;
        }
      }
      return null;
    }
  }

}
