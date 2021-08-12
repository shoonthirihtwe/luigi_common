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
   * 契約ステータス
   * 
   * NU:新契約査定中
   * ーーーーーーーーーーーーーーーーーーーー
   * 40:成立
   * 42:復活査定中
   * 43:復活条件付
   * 45:復活取消
   * 46:復活受付完了
   * 50:クーリングオフ
   * 60:解約
   * 61:保険金支払いによる契約終了
   * 65:解除
   * 70:死亡
   * 71:高度障害（契約終了）
   * 80:満期
   * 90:失効
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ContractStatus {
    CANCEL(60), DELETE(65);

    int val;

    ContractStatus(int val) {
      this.val = val;
    }

    public int toInt() {
      return this.val;
    }
  }

  /**
   * 払込経路
   * 
   * 1:現金 ､ 郵振
   * 2:団体
   * 3:クレジットカード
   * 4:口座振替
   * 5:キャリア
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
