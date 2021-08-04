package jp.co.ichain.luigi2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 保全名義変更顧客情報
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-30
 * @updatedAt : 2021-07-30
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestsTransferVo extends ObjectVo {

  /**
   * 契約者変更区分
   */
  Boolean contractorCustomerChangeFg = false;
  /**
   * 契約者後見人変更区分
   */
  Boolean contractorGuardianCustomerChangeFg = false;
  /**
   * 被保険者変更区分
   */
  Boolean insuredCustomerChangeFg = false;
  /**
   * 被保険者後見人変更区分
   */
  Boolean insuredGuardianCustomerChangeFg = false;
  /**
   * 契約者顧客情報
   */
  MaintenanceRequestsCustomersVo contractorCustomer;

  /**
   * 契約者後見人顧客情報
   */
  MaintenanceRequestsCustomersVo contractorGuardianCustomer;

  /**
   * 被保険者顧客情報
   */
  MaintenanceRequestsCustomersVo insuredCustomer;

  /**
   * 被保険者後見人顧客情報
   */
  MaintenanceRequestsCustomersVo insuredGuardianCustomer;
}
