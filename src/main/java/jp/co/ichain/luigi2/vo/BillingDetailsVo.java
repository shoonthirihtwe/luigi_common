package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BillingDetailsVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-18
 * @updatedAt : 2021-06-18
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BillingDetailsVo extends ObjectVo {

  /**
   * Billing Header ID
   */
  int billingHeaderId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * グロス保険料
   */
  Integer totalGrossPremium;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 請求月
   */
  String billingPeriod;

  /**
   * 充当月
   */
  String dueDate;

  /**
   * 払込方法コード
   */
  String paymentMethodCode;

  /**
   * 保険料 連番
   */
  Integer premiumSequenceNo;

  /**
   * 社員番号
   */
  String customerNo;

  /**
   * 収納代行会社コード
   */
  String factoringCompanyCode;

  /**
   * サイトID
   */
  String siteId;

  /**
   * サイトパスワード
   */
  String sitePass;

  /**
   * ショップID
   */
  String shopId;

  /**
   * ショップパスワード
   */
  String shopPass;

  /**
   * 3Dセキュア表示店舗名
   */
  String tdTenantName;

  /**
   * カード番号(トークン)
   */
  String tokenNo;

  /**
   * カード登録顧客番号 ※証券番号または親証券番号
   */
  String cardCustNumber;

  /**
   * 保険料請求額
   */
  Integer premiumDueAmount;

  /**
   * 保険料充当日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumDueDate;

  Boolean gmApiSuccess;
}
