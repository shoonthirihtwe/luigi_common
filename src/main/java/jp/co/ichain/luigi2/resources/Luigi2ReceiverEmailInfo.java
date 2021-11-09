package jp.co.ichain.luigi2.resources;

/**
 * Receiver情報
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-19
 * @updatedAt : 2021-10-19
 */
public class Luigi2ReceiverEmailInfo {
  /**
   * ReceiverInfo
   *
   * sender_emails_to_tenants:テナント向け送信用メールアドレス 
   * sender_emails_to_clients:クライアント向け送信用メールアドレス
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-20
   * @updatedAt : 2021-10-20
   */
  public enum ReceiverInfo {
    sender_emails_to_tenants, sender_emails_to_clients;
  }

  /**
   * MailType Interface
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-11-09
   * @updatedAt : 2021-11-09
   */
  public interface MailType {

    public String getName();
  }

  /**
   * TenantMailType
   *
   * new_businesses_1st:新契約1次査定者メーリングリスト 
   * new_businesses_2nd:新契約2次査定者メーリングリスト
   * policy_management_1st:保全1次査定者メーリングリスト 
   * policy_management_2nd:保全2次査定者メーリングリスト
   * claim_1st:保険金1次査定メーリングリスト 
   * claim_2nd:保険金2次査定メーリングリスト
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-20
   * @updatedAt : 2021-10-20
   */
  public enum TenantMailType implements MailType {
    new_businesses_1st, new_businesses_2nd, policy_management_1st, policy_management_2nd, claim_1st, claim_2nd;

    @Override
    public String getName() {
      return this.name();
    }
  }

  /**
   * ClientMailType
   *
   * new_business:新契約 
   * policy_management:保全 
   * claim:保険金 
   * billing:請求収納 
   * renewal:更新
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-20
   * @updatedAt : 2021-10-20
   */
  public enum ClientMailType implements MailType {
    new_business, policy_management, claim, billing, renewal;

    @Override
    public String getName() {
      return this.name();
    }
  }
}
