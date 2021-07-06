package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RiskHeadersVo
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
public class RiskHeadersVo extends ObjectVo {

  Integer id;
  
  Integer tenantId;
  
  String contractNo;
  
  String contractBranchNo;
  
  Integer riskSequenceNo;
  
  String activeInactive;
  
  Date riskStartDate;
  
  Date riskEndDate;
  
  String coverageType;
  
  String coverageTerm;
  
  String productStatus;
  
  Date riderAttachedDate;
  
  Date riderInforceDate;
  
  String substandardType;
  
  String substandardAgreementDate;
  
  Date coverageEndDate;
  
  Date exprationDate;
  
  Integer issueAge;
  
  String reinsuranceType;
  
  String reinsuranceCompanyCode;
  
  String benefitCode;
  
  String benefitText;
  
  String paymentCode;
  
  String paymentText;
  
  String otherInsuranceYn;

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
