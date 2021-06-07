package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * UnderWritingsVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ContractsVo extends ObjectVo {

  Integer id;

  Integer tenantId;

  String contractNo;

  String contractBranchNo;

  String contractStatus;

  Integer updateCnt;

  String lastContractId;

  String newContractId;

  Date applicationDate;

  Date receivedDate;

  Date entryDate;

  Date inceptionDate;

  Date completeDate;

  Date firstPremiumDate;

  Date effectiveDate;

  Date issueDate;

  Date expirationDate;

  Date terminationDate;

  Date freeLockDate;

  Date insuranceStartDate;

  Date insuranceEndDate;

  Date premiumStartDate;

  Date premiumEndDate;

  Integer numberOfInsured;

  Integer coverageYear;

  String cardCustNumber;

  Date cardUnavailableFlag;

  String frequency;

  String paymentMethod;

  String product;

  String salesPlanCode;

  String salesPlanTypeCode;

  String basicPolicyCode;

  String hiiOtherInsurance;

  String contractorCustomerId;

  Integer totalPremium;

  String insuredCustomerId;

  String relationship;

  Integer premium;

  String salesMethod;

  String reinsuranceCompCode;

  String researchCompCode;

  String suspendStatus;

  String agencyCode1;

  String agentCode1;

  Integer agentShare1;

  String agencyCode2;

  String agentCode2;

  Integer agentShare2;

  Date mypageLinkDate;

  String paymentPattern;

  Integer paymentDateOrder;

  @JsonIgnore
  Integer updateCount;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;

  @JsonIgnore
  Date deletedAt;

  @JsonIgnore
  String deletedBy;
}
