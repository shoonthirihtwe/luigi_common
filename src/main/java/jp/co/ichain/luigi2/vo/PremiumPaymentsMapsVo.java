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
 * PremiumPaymentsMapsVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-06-04
 * @updatedAt : 2021-06-04
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PremiumPaymentsMapsVo extends ObjectVo {

  Integer id;

  Integer tenantId;

  String salesPlanCode;

  String salesPlanTypeCode;

  Date startDate;

  Date endDate;

  Integer frequency;

  String paymentMethod;

  String paymentPattern;

  Integer paymentDateOrder;

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
