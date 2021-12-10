package jp.co.ichain.luigi2.resources.code;

/**
 * 共通コード
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeCommon {

  /**
   * フラグ
   * 
   * 0:FALSE 
   * 1:TRUE
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum FlagCode {
    FALSE("0"), TRUE("1");

    String val;

    FlagCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * SexCode
   * 
   * 1:男子 
   * 2:女子 
   * 3:法人 
   * 4:その他
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum SexCode {
    MAN("1"), WOMAN("2"), CORPORATE("3"), ETC("4");

    String val;

    SexCode(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }

  /**
   * テーブルデータ取得タイプ
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-11-17
   * @updatedAt : 2021-11-17
   */
  public enum TableType {
    contracts("C"), claim_payment("CH"), claim_detail_payment("CD"), refund_payment("RA");

    String val;

    TableType(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }

  }
}
