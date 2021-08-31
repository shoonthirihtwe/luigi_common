package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ContactBillingVo
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
public class ContractBillingVo extends ObjectVo {
  /**
   * tenantId
   */
  String tenantId;

  /**
   * 決済周期
   */
  String paymentPattern;
  
  /**
   * 決済日序数
   */
  String paymentDateOrder;
  /**
   * 請求月
   */
  Date batchDate;

  /**
   * 払込方法コード
   */
  String paymentMethodCode;

  /**
   * 連番
   */
  Integer billngHeaderNo;

  /**
   * 請求ヘッダー状態コード
   */
  String billingHeaderStatus;

  /**
   * 収納代行会社コード
   */
  String factoringCompanyCode;

  /**
   * 請求額
   */
  Integer totallBillerdAmount;

  /**
   * 入金額
   */
  Integer totalReceivedAamount;

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
  String premiumDueAmount;

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
}
