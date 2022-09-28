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

  /**
   * ID
   */
  Integer id;
  
  /**
   * テナントID
   */
  Integer tenantId;
  
  /**
   * 証券番号
   */
  String contractNo;
  
  /**
   * 証券番号枝番
   */
  String contractBranchNo;
  
  /**
   * 連番
   */
  Integer riskSequenceNo;
  
  /**
   * 有効/無効フラグ
   */
  String activeInactive;
  
  /**
   * 開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date riskStartDate;
  
  /**
   * 終了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date riskEndDate;
  
  /**
   * 保険期間のタイプ
   */
  String coverageType;
  
  /**
   * 保険期間（年数・歳）
   */
  String coverageTerm;
  
  /**
   * 契約のステータス
   */
  String productStatus;
  
  /**
   * 特約付加日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date riderAttachedDate;
  
  /**
   * 特約有効日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date riderInforceDate;
  
  /**
   * 特別条件タイプ
   */
  String substandardType;
  
  /**
   * 特別条件了承日
   */
  String substandardAgreementDate;
  
  /**
   * 保証期間終了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date coverageEndDate;
  
  Date exprationDate;
  
  /**
   * 被保険者　契約年齢
   */
  Integer issueAge;
  
  /**
   * 再保険タイプ
   */
  String reinsuranceType;
  
  /**
   * 再保険会社コード
   */
  String reinsuranceCompanyCode;
  
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
   * 他保険有無
   */
  String otherInsuranceYn;

  /**
   * ロック用
   */
  Integer updateCount;
  
  /**
   * 基本給付額
   */
  Integer benefitBaseAmount;
  
  /**
   * 基本給付額単位
   */
  String benefitBaseUnit;
  
  /**
   * 保険金・給付金グループタイプ
   */
  String benefitGroupType;

  /*
   * 通算使用値
   */
  Integer sumUpValue;

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
   * 保障消滅基準日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date terminationBaseDate;
  
  /**
   * 保障消滅日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date terminationDate;
  
  /**
   * 保障消滅事由
   */
  String terminationTitle;
  
  /**
   * 支払金額ベース
   */
  String calcBase;
  
  /**
   * compense_rate
   */
  Integer compenseRate;
  
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
}
