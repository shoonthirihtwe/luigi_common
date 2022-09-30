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
 * BenefitsVo
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
public class BenefitsVo extends ObjectVo {

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
  String benefitCode;
  /**
   * 保険金名称
   */
  String benefitText;
  /**
   * 支払タイプ
   */
  String paymentCode;
  /**
   * 支払タイプ名称
   */
  String paymentText;
  /**
   * 保険金注記
   */
  String benefitNote;
  /**
   * 支払金額ベース
   */
  String calcBase;
  /**
   * 保障割合
   */
  Integer compenseRate;
  /**
   * 基本給付額
   */
  Integer benefitBaseAmount;
  /**
   * 基本給付額単位
   */
  String benefitBaseUnit;
  /**
   * 免責額・自己負担額
   */
  Integer disclaimAmount;
  /**
   * 免責額・自己負担額単位
   */
  String disclaimUnit;
  /**
   * 都度支払限度額
   */
  Integer paygLimitAmount;
  /**
   * 通算支払限度額
   */
  Integer totalLimitAmount;
  /**
   * 支払限度超過後の契約状態
   */
  String afterOverLimit;
  /**
   * 免責日数
   */
  Integer disclaimDays;
  /**
   * 通算限度日数・回数
   */
  Integer totalLimitDays;
  /**
   * 待機日数
   */
  Integer waitingDays;
  /**
   * 保険金・給付金グループタイプ
   */
  String benefitGroupTypeBl;
  /**
   * 保険金・給付金グループタイプ
   */
  String benefitGroupTypeBylaw;
  /*
   * 通算使用値
   */
  Integer sumUpValue;

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
