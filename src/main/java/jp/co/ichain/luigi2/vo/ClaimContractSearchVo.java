package jp.co.ichain.luigi2.vo;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ClaimContractSearchVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ClaimContractSearchVo extends ObjectVo {
  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 請求者情報
   */
  ClaimHeadersVo claimHeader;

  /**
   * 被保険者
   */
  ClaimCustomerVo insured;

  /**
   * 死亡保険金受取人
   */
  List<ClaimCustomerVo> beneficiaries;

  /**
   * 保障内容Vo
   */
  List<RiskHeadersVo> benefits;

  public ClaimContractSearchVo() {
    benefits = new ArrayList<RiskHeadersVo>();
  }
}
