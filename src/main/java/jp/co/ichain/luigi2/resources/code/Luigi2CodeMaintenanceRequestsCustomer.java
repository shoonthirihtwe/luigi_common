package jp.co.ichain.luigi2.resources.code;

/**
 * 保全申請コード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
public class Luigi2CodeMaintenanceRequestsCustomer {

  /**
   * ロール
   * PH:契約者
   * IN:被保険者
   * BE:保険金受取人(給付タイプ）
   * BD:死亡保険金受取人
   * BM:満期保険金受取人
   * PG:契約者成年後見人等
   * IG:被保険者成年後見人等
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum Role {
    PH, IN, BE, BD, BM, PG, IG
  }
}
