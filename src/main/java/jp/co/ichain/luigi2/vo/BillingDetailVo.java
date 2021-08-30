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
 * BillingDetailVo
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-06-18
 * @updatedAt : 2021-06-18
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BillingDetailVo {
  /**
   * id
   */
  String id;

  /**
   * tenantId
   */
  String tenantId;

  /**
   * 請求月
   */
  String billingPeriod;

  /**
   * 払込方法コード
   */
  String paymentMethodCode;

  /**
   * 連番
   */
  Integer billngHeaderNo;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 保険料充当日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumDueDate;

  /**
   * 保険料 連番
   */
  Long premiumSequenceNo;

  /**
   * 充当月
   */
  String dueDate;

  /**
   * 保険料請求額
   */
  Integer premiumDueAmount;

  /**
   * 銀行コード
   */
  String bankCode;

  /**
   * 支店コード
   */
  String bankBranchCode;

  /**
   * 口座種別
   */
  String bankAccountType;

  /**
   * 口座番号
   */
  String bankAccountNo;

  /**
   * カード番号(トークン)
   */
  String tokenNo;

  /**
   * ロック用
   */
  String bankResultCode;

  /**
   * ロック用
   */
  String cardResultCode;

  /**
   * ロック用
   */
  String otherResultCode;

  /**
   * ロック用
   */
  String nameKnjSei;

  /**
   * ロック用
   */
  String nameKnjMei;

  /**
   * ロック用
   */
  String nameKanaSei;

  /**
   * ロック用
   */
  String nameKanaMei;

  /**
   * ロック用
   */
  Integer updateCount;

  /**
   * 作成者
   */
  @JsonIgnore
  String createdBy;

  /**
   * 最終更新者
   */
  @JsonIgnore
  String updatedBy;
}
