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
 * DepositDetailsVo
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
public class DepositDetailsVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

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
   * 明細番号
   */
  Integer cashDetailNo;

  /**
   * 契約番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 申込番号
   */
  String applicationNo;

  /**
   * 充当月
   */
  String dueDate;

  /**
   * 合計保険料金額
   */
  Integer totalPremiumAmount;

  /**
   * 入金金額
   */
  Integer depositAmount;

  /**
   * 振込手数料
   */
  Integer commissionWithheld;

  /**
   * 振込手数料 消費税
   */
  Integer compensationTax;

  /**
   * 消し込み日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date clearingDate;

  /**
   * サスペンス日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date suspenceDate;

  /**
   * 削除日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date deleteDate;

  /**
   * マッチング日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date cashMatchingDate;

  /**
   * 明細のステータス
   */
  String cashDetailStatus;

  /**
   * 引き去り結果コード
   */
  String paymentResultCode;

  /**
   * 取引ID
   */
  String accessId;

  /**
   * 取引パスワード
   */
  String accessPass;

  /**
   * 備考
   */
  String comment;

  /**
   * 保険料充当日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumDueDate;

  /**
   * 保険料 連番
   */
  Integer premiumSequenceNo;

  /**
   * 取引用ID
   */
  String factoringTransactionId;

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
