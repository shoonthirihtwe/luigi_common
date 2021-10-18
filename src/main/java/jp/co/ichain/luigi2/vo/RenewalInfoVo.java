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

/***
 * RenewalInfoVo
 *
 * @author : [VJP] n.huy.hoang
 * @createdAt : 2021-07-06
 * @updatedAt : 2021-07-06
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class RenewalInfoVo {

  Integer id;

  Integer tenantId;

  /**
   * 更新前証券番号
   */
  String contractNo;

  /**
   * 更新前証券番号枝番
   */
  String contractBranchNo;

  /**
   * 更新後証券番号
   */
  String renewalContractNo;

  /**
   * 更新後証券番号枝番
   */
  String renewalContractBranchNo;

  /**
   * 契約者ID
   */
  String contractorCustomerId;

  /**
   * 契約者名
   */
  String nameKnj;

  /**
   * 非保険ID
   */
  String insuredCustomerId;

  /**
   * 被保険者名
   */
  String insNameKnj;

  /**
   * 更新回数
   */
  Integer updateCnt;

  /**
   * 更新判定日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalJudgeDate;
  
  /**
   * 更新ステータス
   */
  String renewalStatus;

  /**
   * 反社チェック
   */
  String renewalAntisocialForcesCheck;

  /**
   * 更新日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalDate;

  /**
   * 受付日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  /**
   * 現契約日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date issueDate;

  /**
   * 更新後満期日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalExpirationDate;

  /**
   * 更新後満期日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date expirationDate;

  /**
   * 販売プランコード
   */
  String salesPlanCode;

  /**
   * 保険商品・プラン
   */
  String salesPlanName;

  /**
   * 販売プラン種別コード
   */
  String salesPlanTypeCode;

  /**
   * 販売プラン口数
   */
  String numberOfSalesPlan;

  /**
   * 保険期間
   */
  String coverageYear;

  /**
   * 更新後年齢
   */
  Integer renewalAge;

  /**
   * １回分保険料(現行)
   */
  Integer premiumAmount;

  /**
   * 
   */
  Integer premium;

  /**
   * １回分保険料(更新後)
   */
  Integer renewalPremiumAmount;

  /**
   * 更新タイプ
   */
  String renewalType;

  /**
   * 更新有無
   */
  String renewalRejection;

  /**
   * ロック用
   */
  Integer updateCount;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
