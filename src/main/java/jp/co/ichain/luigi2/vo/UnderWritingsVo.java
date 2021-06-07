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
public class UnderWritingsVo extends ObjectVo {

  Integer id;

  Integer tenantId;

  String contractNo;

  String contractStatus;

  String rejectFlag;

  Date applicationDate;

  Date receivedDate;

  Date effectiveDate;

  String sumingUpCheck;

  String antisocialForcesCheck;

  String questionCheck;

  String otherCheck;

  String cardCustNumber;

  Date cardAnavailableFlag;

  String firstAssessmentResults;

  String secondAssessmentResults;

  String commnt1stUw;

  String commnt2ndUw;

  String communicationColumn;

  Date notificationToApplicantDate;

  String salesPlanCode;

  String salesPlanTypeCode;

  String materialRepresentation;

  String understandingIntent;

  String confirmApplication;

  String contractorCustomerId;

  String insuredCustomerId;

  String question1;

  String question2;

  String question3;

  String question4;

  String question5;

  String question6;

  String question7;

  String question8;

  String question1Text;

  String question2Text;

  String question3Text;

  String question4Text;

  String question5Text;

  String question6Text;

  String question7Text;

  String question8Text;

  String substandardReply;

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