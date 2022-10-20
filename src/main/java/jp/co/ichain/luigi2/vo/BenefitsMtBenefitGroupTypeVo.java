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
 * BenefitsMtBenefitGroupTypeVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-28
 * @updatedAt : 2021-10-28
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BenefitsMtBenefitGroupTypeVo extends ObjectVo {

  /**
   * ID
   */
  Integer id;
  /**
   * テナントID
   */
  Integer tenantId;
  /**
   * 販売プランコード
   */
  String salesPlanCode;
  /**
   * 販売プラン種別コード
   */
  String salesPlanTypeCode;
  /**
   * 保険金タイプ
   */
  String benefitGroupType;
  /*
   * 連番
   */
  String riskSequenceNo;

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

  /**
   * ロック用
   */
  Integer updateCount;
}
