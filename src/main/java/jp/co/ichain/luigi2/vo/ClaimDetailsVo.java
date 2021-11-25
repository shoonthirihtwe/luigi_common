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
 * ClaimDetailsVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-23
 * @updatedAt : 2021-06-23
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDetailsVo extends ObjectVo {


  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 保険金・給付金情報ID
   */
  Integer claimTrxsId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;
  
  /**
   * 保障連番
   */
  Integer riskSequenceNo;

  /**
   * 連番
   */
  Integer sequenceNo;

  /**
   * 有効/無効フラグ
   */
  String activeInactive;

  /**
   * 主契約／特約区分
   */
  String baseRider;

  /**
   * 商品タイプ
   */
  String productType;

  /**
   * 保険種別コード
   */
  String policyCode;

  /**
   * 商品バージョン
   */
  String version;

  /**
   * 保険プラン
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
   * 請求対象期間(from)
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date treatmentDateFrom;
  
  /**
   * 請求対象期間(to)
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date treatmentDateTo;
  
  /**
   * 都度支払限度額
   */
  Integer paygLimitAmount;

  /**
   * 給付日額
   */
  Integer dailyBenefitAmount;

  /**
   * 請求内容
   */
  String claimDetailInfo;
  
  /**
   * 請求対象回数
   */
  Integer treatmentTimes;

  /**
   * 請求日数
   */
  Integer claimDays;

  /**
   * 請求額
   */
  Integer claimAmount;

  /**
   * 保険対象外金額
   */
  Integer noncoverageAmount;

  /**
   * 保険対象金額
   */
  Integer coverageAmount;

  /**
   * 保障割合
   */
  Integer compenseRate;

  /**
   * 免責額・自己負担額
   */
  Integer disclaimAmount;

  /**
   * 都度支払限度額
   */
  Integer eachtimeLimitAmount;

  /**
   * 通算支払限度額
   */
  Integer totalLimitAmount;

  /**
   * 支払対象額
   */
  Integer benefitAmount;

  /**
   * 通算超過額
   */
  Integer overAmount;

  /**
   * 他社支払額
   */
  Integer othersPaidAmount;

  /**
   * 支払決定額
   */
  Integer amountToPay;

  /**
   * 他保険
   */
  String otherInsuerer;

  /**
   * その他商品調整額
   */
  Integer customProductAdjustment;

  /**
   * その他商品調整額題目
   */
  String customProductAdjustmentTitle;

  /**
   * ロック用
   */
  Integer updateCount;

  /**
   * 作成日時
   */
  @JsonIgnore
  Date createdAt;

  /**
   * 作成者
   */
  @JsonIgnore
  String createdBy;

  /**
   * 最終更新日時
   */
  @JsonIgnore
  Date updatedAt;

  /**
   * 最終更新者
   */
  @JsonIgnore
  String updatedBy;

  /**
   * 論理削除
   */
  @JsonIgnore
  Date deletedAt;

  /**
   * 論理削除者
   */
  @JsonIgnore
  String deletedBy;
  
  /**
   * 基本給付額単位
   */
  String benefitBaseUnit;
}
