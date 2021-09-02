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
 * RenewalInfoForNoticationVo
 *
 * @author : [VJP] n.huy.hoang
 * @createdAt : 2021-07-08
 * @updatedAt : 2021-07-08
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class RenewalInfoForNoticationVo {

  Integer contractId;
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
   * 更新判定日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalJudgeDate;

  /**
   * 更新ステータス
   */
  String renewalStatus;

  /**
   * sendee
   */
  String sendee;
  
  /**
   * 更新タイプ
   */
  String renewalType;

  /**
   * 更新日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date renewalDate;

  /**
   * 販売プランコード
   */
  String salesPlanCode;

  /**
   * 販売プラン種別コード
   */
  String salesPlanTypeCode;

  /**
   * 反社チェック
   */
  String renewalAntisocialForcesCheck;

  /**
   * 更新後年齢
   */
  String renewalAge;

  /**
   * １回分保険料(現行)
   */
  Integer premiumAmount;

  /**
   * １回分保険料(更新後)
   */
  Integer renewalPremiumAmount;

  String nameKnjSei;

  String nameKnjMei;

  /**
   * ジーメール
   */
  String email;

  /**
   * 契約日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date issueDate;

  /**
   * 保険料
   */
  Integer totalPremium;
  
  /**
   * 満了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date expirationDate;

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
