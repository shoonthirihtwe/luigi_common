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
 * ClaimHeaderVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-16
 * @updatedAt : 2021-06-16
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ClaimHeadersVo extends ObjectVo {

  Integer id;

  Integer tenantId;

  Integer claimTrxsId;

  String contractNo;

  String contractBranchNo;

  String activeInactive;

  Date claimDate;

  Date receivedDate;

  String baseRider;

  String productType;

  String policyCode;

  String version;

  String claimCategory;

  String claimantSeiKnj;

  String claimantMeiKnj;

  String claimantSeiKana;

  String claimantMeiKana;

  Date claimantDateOfBirth;

  String claimantAddress;

  String relationship;

  String resultCode;

  String parentalKanji;

  String parentalKana;

  String parentalRelationship;

  String telNo;

  String contactTelNo;

  String email;

  String typeOfAccident;

  Date accidentDate;

  String accidentPlace;

  String accidentInfo;

  String bankName;

  String bankCode;

  String bankBranchName;

  String bankBranchCode;

  String bankAccountType;

  String bankAccountNo;

  String bankAccountHolder;

  Integer claimTotalAmount;

  Integer benefitTotalAmount;

  Integer numberOfPaid;

  Integer paidAmount;

  Integer overTotalAmount;

  Integer amountToPay;

  String claimStatus;

  String underwritingStatus;

  Date claimStatusDate;

  Date underwritingDate;

  String receptionComment;

  String firstUnderwriting;

  Date firstUnderwriter;

  String firstUnderwritingComment;

  String secondUnderwriting;

  Date secondUnderwriter;

  String secondUnderwritingComment;

  String information;

  String unratedReason;

  String inspection;

  Date dueDate;

  Date paymentDate;

  Date setbackDate;

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
