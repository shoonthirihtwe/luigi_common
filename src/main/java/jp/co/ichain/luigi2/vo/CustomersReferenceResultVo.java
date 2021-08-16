package jp.co.ichain.luigi2.vo;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * CustomersReferenceResultVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-07-09
 * @updatedAt : 2021-07-09
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class CustomersReferenceResultVo extends ObjectVo {
  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 顧客ID
   */
  String customerId;

  /**
   * 証券
   */
  List<CustomersReferenceVo> contracts;

  /**
   * 顧客
   */
  List<PolicyHolderReferenceVo> customers;

  public CustomersReferenceResultVo() {
    contracts = new ArrayList<CustomersReferenceVo>();
    customers = new ArrayList<PolicyHolderReferenceVo>();
  }
}
