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
 * AgencyCommissionsPaidVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-07-05
 * @updatedAt : 2021-07-05
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AgencyCommissionsPaidVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 代理店コード
   */
  String agencyCode;

  /**
   * 計上年月
   */
  String recordDate;

  /**
   * 支払手数料額合計_初年度
   */
  Integer commissionAmountF;

  /**
   * 手数料消費税額合計_初年度
   */
  Integer commissionTaxF;

  /**
   * 支払手数料額合計_2年目以降
   */
  Integer commissionAmountS;

  /**
   * 手数料消費税額合計_2年目以降
   */
  Integer commissionTaxS;

  /**
   * 戻入手数料額合計
   */
  Integer refundAmount;

  /**
   * 戻入消費税額合計
   */
  Integer refundTax;

  /**
   * 手数料支払額
   */
  Integer paidAmount;

  /**
   * 支払消費税額
   */
  Integer paidTax;

  /**
   * 源泉徴収税額
   */
  Integer withholdTax;

  /**
   * 手数料対象保険料合計_初年度
   */
  Integer premiumAmountF;

  /**
   * 手数料対象保険料件数_初年度
   */
  Integer premiumCountF;

  /**
   * 手数料対象保険料合計_2年目以降
   */
  Integer premiumAmountS;

  /**
   * 手数料対象保険料件数_2年目以降
   */
  Integer premiumCountS;

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
}
