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
 * RefundAmountVo
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
public class RefundAmountVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 保全申請番号
   */
  String requestNo;

  /**
   * 有効/無効フラグ
   */
  String activeInactive;

  /**
   * 支払事由
   */
  String payReason;

  /**
   * 支払先
   */
  String payMethod;

  /**
   * 支払先金融機関名
   */
  String bankName;

  /**
   * 支払先金融機関コード
   */
  String bankCode;

  /**
   * 支払先金融機関支店名
   */
  String bankBranchName;

  /**
   * 支払先金融機関支店コード
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
   * 口座名義人
   */
  String bankAccountHolder;

  /**
   * 提携先コード
   */
  String partnerCode;

  /**
   * お客様番号
   */
  String payCustomerCode;

  /**
   * 確認番号
   */
  String confirmNo;

  /**
   * 問い合わせ番号
   */
  String inquiryNo;

  /**
   * その他番号
   */
  Integer otherCode;

  /**
   * 解約払戻金額
   */
  Integer cashValue;

  /**
   * 保険料返金額
   */
  Integer refundAmount;

  /**
   * 解約手数料
   */
  Integer surrenderCharge;

  /**
   * 税額
   */
  Integer taxAmount;

  /**
   * 支払対象総額
   */
  Integer totalRefundAmount;

  /**
   * 支払予定日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date dueDate;

  /**
   * 支払処理日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date paymentDate;

  /**
   * 組戻し処理日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date setbackDate;

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
