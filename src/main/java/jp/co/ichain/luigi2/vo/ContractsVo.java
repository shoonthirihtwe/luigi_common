package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
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

  @JsonSerialize(using = JsonDateSerializer.class)
  Date applicationDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date entryDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date inceptionDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date completeDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date firstPremiumDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date effectiveDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date issueDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date expirationDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date terminationDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date freeLockDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date insuranceStartDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date insuranceEndDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumStartDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumEndDate;
  
  Integer numberOfInsured;

  Integer coverageTerm;

  String cardCustNumber;

  @JsonSerialize(using = JsonDateSerializer.class)
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

  @JsonSerialize(using = JsonDateSerializer.class)
  Date mypageLinkDate;

  String paymentPattern;

  Integer paymentDateOrder;

  Integer updateCount;
  
  /**
   * 更新日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalDate;
  
  /**
   * 更新後満期日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalExpirationDate;
  
  /**
   * １回分保険料(更新後)
   */
  Integer renewalPremiumAmount;
  
  /**
   * 更新後年齢
   */
  Integer renewalAge;

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
