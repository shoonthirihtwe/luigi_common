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
 * AgenciesVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-07-01
 * @updatedAt : 2021-07-01
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AgenciesVo extends ObjectVo {
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
   * 財務局登録代理店コード
   */
  String offcialAgencyCode;

  /**
   * 個人/法人フラグ
   */
  String corporationIndividualFlag;

  /**
   * 登録種別
   */
  String corporationType;

  /**
   * corporate_number
   */
  String corporateNumber;

  /**
   * 源泉徴収区分
   */
  String withholdingTax;

  /**
   * 代理店名
   */
  String agencyNameKnj;

  /**
   * 代理店名（カナ）
   */
  String agencyNameKana;

  /**
   * 屋号
   */
  String shopName;

  /**
   * 郵便番号
   */
  String zipCode;

  /**
   * 住所
   */
  String address;

  /**
   * 代表電話番号
   */
  String repTel;

  /**
   * 代表者名
   */
  String repNameKnj;

  /**
   * 代表者名（カナ）
   */
  String repNameKana;

  /**
   * 代表者生年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date repDateOfBirth;

  /**
   * 担当者名
   */
  String personInChargeKnj;

  /**
   * 担当者名（カナ）
   */
  String personInChargeKana;

  /**
   * 担当者電話番号
   */
  String personInChargeTel;

  /**
   * 担当者メールアドレス
   */
  String personInChargeEmail;

  /**
   * 教育責任者 漢字
   */
  String personInChargeEmKnj;

  /**
   * 教育責任者 カナ
   */
  String personInChargeEmKana;

  /**
   * 管理責任者 漢字
   */
  String personInChargeMgKnj;

  /**
   * 管理責任者 カナ
   */
  String personInChargeMgKana;

  /**
   * 稼働開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDate;

  /**
   * 代理店フラグ
   */
  String agencyFlag;

  /**
   * 総代理店フラグ
   */
  String generalAgentFlag;

  /**
   * 手数料クラス
   */
  String commissionClass;

  /**
   * 手数料モード
   */
  String commissionMode;

  /**
   * 登録日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date registrationDate;

  /**
   * 廃業日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date terminationDate;

  /**
   * 金融機関コード
   */
  String bankCode;

  /**
   * 支店コード
   */
  String bankBranchCode;

  /**
   * type_of_account
   */
  String typeOfAccount;

  /**
   * 口座番号
   */
  String bankAccountNo;

  /**
   * bank_account_holder
   */
  String bankAccountHolder;

  /**
   * 代理店ステータス
   */
  Integer agencyStatus;

  /**
   * memo
   */
  String memo;

  /**
   * rep_name_sei_knj
   */
  String repNameSeiKnj;

  /**
   * rep_name_mei_knj
   */
  String repNameMeiKnj;

  /**
   * rep_name_sei_kana
   */
  String repNameSeiKana;

  /**
   * rep_name_mei_kana
   */
  String repNameMeiKana;

  /**
   * person_in_charge_sei_knj
   */
  String personInChargeSeiKnj;

  /**
   * person_in_charge_mei_knj
   */
  String personInChargeMeiKnj;

  /**
   * person_in_charge_sei_kana
   */
  String personInChargeSeiKana;

  /**
   * person_in_charge_mei_kana
   */
  String personInChargeMeiKana;

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
   * ロック用
   */
  @JsonIgnore
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
