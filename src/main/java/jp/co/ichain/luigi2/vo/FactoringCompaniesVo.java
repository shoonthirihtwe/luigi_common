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
 * 収納代行会社マスタ
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-09-08
 * @updatedAt : 2021-09-08
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class FactoringCompaniesVo extends ObjectVo {

  /**
   * テナントID
   */
  Integer tenantId;
  /**
   * 収納代行会社コード
   */
  String factoringCompanyCode;
  /**
   * 収納代行会社名
   */
  String factoringCompanyName;
  /**
   * 払込経路
   */
  String paymentMethod;
  /**
   * 開始日
   */
  Date factoringCompanyStartDate;
  /**
   * 終了日
   */
  Date factoringCompanyEndDate;
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
   * 請求月
   */
  String billingMonth;

  /**
   * 通帳記載内容
   */
  String passbookEntry;
  /**
   * 請求基準日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date billingRecordDate;
}
