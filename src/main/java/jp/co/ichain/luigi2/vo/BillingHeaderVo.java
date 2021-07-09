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
 * BillingHeaderVo
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
public class BillingHeaderVo {
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
   * 請求ヘッダー状態コード
   */
  String billingHeaderStatus;

  /**
   * 団体コード
   */
  String groupCode;

  /**
   * 収納代行会社コード
   */
  String factoringCompanyCode;

  /**
   * 作成日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date createDate;

  /**
   * 請求額
   */
  Integer totallBillerdAmount;

  /**
   * 入金額
   */
  Integer totalReceivedAmount;

  /**
   * 受付日
   */

  Date receivedDate;

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
