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
 * DepositVo
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-06-29
 * @updatedAt : 2021-06-29
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DepositVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * DepositDetails ID
   */
  Integer idDepositDetails;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * batch date
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date batchDate;

  /**
   * 未納分保険料月
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumDueDate;

  /**
   * 引き去り結果コード
   */
  String paymentResultCode;

  /**
   * 合計保険料金額
   */
  Integer totalPremiumAmount;

  /**
   * 入力日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date entryDate;

  /**
   * バッチナンバー
   */
  Integer batchNo;

  /**
   * 払込方法コード
   */
  String paymentMethodCode;

  /**
   * 入金日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date depositDate;

  /**
   * 入金金額
   */
  String receivedAmount;

  /**
   * バッチ合計金額
   */
  String batchTotalAmount;

  /**
   * ステータス
   */
  String batchStatus;

  /**
   * 備考
   */
  String comment;

  /**
   * 処理ユーザーID
   */
  String usereId;

  /**
   * 収集ルート
   */
  String collectionRoute;

  /**
   * 団体コード
   */
  String groupCode;

  /**
   * ロック用
   */
  Integer updateCount;

  /**
   * 作成者
   */
  String createdBy;

  /**
   * 最終更新者
   */
  String updatedBy;
}
