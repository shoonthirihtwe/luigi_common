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
 * PolicyHoldersPayMethodVo
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
public class PolicyHoldersPayMethodVo extends ObjectVo {

  Integer id;
  
  Integer tenantId;
  
  String contractNo;
  
  Date startDate;
  
  Date endDate;
  
  String paymentMethod;
  
  String status;
  
  String departmentCode;
  
  String customerNo;
  
  String factoringCompanyCode;
  
  String bankCode;
  
  String bankBranchCode;
  
  String bankAccountType;
  
  String bankAccountNo;
  
  String tokenNo;

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
