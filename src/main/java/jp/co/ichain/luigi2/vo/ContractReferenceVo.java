package jp.co.ichain.luigi2.vo;

import java.util.Date;
import java.util.List;
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
 * ContractReferenceVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-07-09
 * @updatedAt : 2021-07-09
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ContractReferenceVo extends ObjectVo {
  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 顧客ID
   */
  String customerId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 申込年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date applicationDate;

  /**
   * 受付年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  /**
   * 契約開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date inceptionDate;

  /**
   * 契約ステータス
   */
  String contractStatus;

  /**
   * 販売プランコード
   */
  String salesPlanCode;

  /**
   * 販売プラン種別コード
   */
  String salesPlanTypeCode;

  /**
   * 代理店コード1
   */
  String agencyCode1;

  /**
   * 募集人コード1
   */
  String agentCode1;

  /**
   * 募集割合1
   */
  Integer agentShare1;

  /**
   * 代理店コード2
   */
  String agencyCode2;

  /**
   * 募集人コード2
   */
  String agentCode2;

  /**
   * 募集割合2
   */
  Integer agentShare2;

  /**
   * 契約者ID
   */
  @JsonIgnore
  String contractorCustomerId;

  /**
   * 被保険者ID
   */
  @JsonIgnore
  String insuredCustomerId;

  /**
   * 支払方法
   */
  String paymentMethodCode;

  /**
   * 保険料払込回数
   */
  String frequency;

  /**
   * 保険期間（始）
   */
  @JsonIgnore
  Date insuranceStartDate;

  /**
   * 保険期間（終）
   */
  @JsonIgnore
  Date insuranceEndDate;

  /**
   * 契約者情報
   */
  PolicyHolderReferenceVo policyHolder;

  /**
   * 被保険者情報
   */
  InsuredReferenceVo insured;

  /**
   * 受取人情報
   */
  List<BeneficialiesVo> beneficiaries;

  /**
   * 固有データ
   */
  String data;
}
