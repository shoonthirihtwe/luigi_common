package jp.co.ichain.luigi2.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ClaimHeaderVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-16
 * @updatedAt : 2021-06-16
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ClaimHeadersVo extends ObjectVo {


  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 保険金・給付金情報ID
   */
  Integer claimTrxsId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 有効/無効フラグ
   */
  String activeInactive;

  /**
   * 請求日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date claimDate;

  /**
   * 受付日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  /**
   * 主契約／特約区分
   */
  String baseRider;

  /**
   * 商品タイプ
   */
  String productType;

  /**
   * 保険種別コード
   */
  String policyCode;

  /**
   * 商品バージョン
   */
  String version;

  /**
   * 請求者区分
   */
  String claimantCategory;

  /**
   * 漢字氏名
   */
  String claimantKnj;

  /**
   * カナ氏名
   */
  String claimantKana;

  /**
   * 請求者（被保険者） 姓
   */
  String claimantSeiKnj;

  /**
   * 請求者（被保険者） 名
   */
  String claimantMeiKnj;

  /**
   * 請求者（被保険者）セイ
   */
  String claimantSeiKana;

  /**
   * 請求者（被保険者）メイ
   */
  String claimantMeiKana;

  /**
   * 請求者生年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date claimantDateOfBirth;

  /**
   * 請求者住所1(漢字)
   */
  String claimantAddrKnj1;

  /**
   * 請求者住所2(漢字)
   */
  String claimantAddrKnj2;

  /**
   * 請求者郵便番号
   */
  String claimantAddrZipCode;

  /**
   * 請求者住所―県(漢字)
   */
  String claimantAddrKnjPref;

  /**
   * 請求者（被保険者）との続柄
   */
  String relationship;

  /**
   * 請求者反社チェック結果コード
   */
  String resultCode;

  /**
   * フロント側で反社チェック結果判断するフラグ
   */
  boolean antiSocialFlag;

  /**
   * 親権者（被保険者）
   */
  String parentalKanji;

  /**
   * 親権者（被保険者） カナ
   */
  String parentalKana;

  /**
   * 親権者（被保険者）との続柄
   */
  String parentalRelationship;

  /**
   * 請求者電話番号
   */
  String telNo;

  /**
   * 請求者電話番号日中連絡先
   */
  String contactTelNo;

  /**
   * 請求者メールアドレス
   */
  String email;

  /**
   * 請求事由発生日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date accidentDate;

  /**
   * 請求事由発生場所
   */
  String accidentPlace;

  /**
   * 請求事由内容
   */
  String accidentInfo;

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
   * 請求総額
   */
  Integer claimTotalAmount;

  /**
   * 支払対象総額
   */
  Integer benefitTotalAmount;

  /**
   * 既払回数
   */
  Integer numberOfPaid;

  /**
   * 既払通算額
   */
  Integer paidAmount;

  /**
   * 通算限度超過総額
   */
  Integer overTotalAmount;

  /**
   * 支払決定額
   */
  Integer amountToPay;

  /**
   * 申請ステータス
   */
  String claimStatus;

  /**
   * 査定ステータス
   */
  String underwritingStatus;

  /**
   * 申請ステータス更新日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date claimStatusDate;

  /**
   * 査定ステータス更新日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date underwritingDate;

  /**
   * 受付者コメント
   */
  String receptionComment;

  /**
   * 1次査定者
   */
  String firstUnderwriting;

  /**
   * 1次査定日時
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date firstUnderwriter;

  /**
   * 査定コメント1次
   */
  String firstUnderwritingComment;

  /**
   * 2次査定者
   */
  String secondUnderwriting;

  /**
   * 2次査定日時
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date secondUnderwriter;

  /**
   * 査定コメント2次
   */
  String secondUnderwritingComment;

  /**
   * 通信欄コメント
   */
  String information;

  /**
   * 自動判定不可理由 [0]-[9]
   */
  String unratedReason;

  /**
   * 調査要否
   */
  String inspection;

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
   * 未経過保険料
   */
  Integer refundPremium;

  /**
   * 未収保険料
   */
  Integer receivablePremium;

  /**
   * 未経過保険料調整額
   */
  Integer refundPremiumAdjustment;

  /**
   * 未経過保険料調整額題目
   */
  String refundPremiumAdjustmentTitle;

  /**
   * 未収保険料調整額
   */
  Integer receivablePremiumAdjustment;

  /**
   * 未収保険料調整額題目
   */
  String receivablePremiumAdjustmentTitle;

  /**
   * その他保険料調整額
   */
  Integer customPremiumAdjustment;

  /**
   * その他保険料調整額題目
   */
  String customPremiumAdjustmentTitle;

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

  /**
   * 商品名
   */
  String salesPlanName;

  /**
   * 請求者氏名
   */
  String claimantNameKnj;

  /**
   * 請求者氏名（カナ）
   */
  String claimantNameKana;

  /**
   * 固有データ
   */
  String data;

  /**
   * 請求内容
   */
  @JsonIgnore
  String claimDetailInfo;

  /**
   * 請求額
   */
  @JsonIgnore
  Integer claimAmount;

  /**
   * 受診回数
   */
  @JsonIgnore
  Integer treatmentTimes;

  /**
   * 支払対象額
   */
  @JsonIgnore
  Integer benefitAmount;

  /**
   * 他保険
   */
  @JsonIgnore
  String otherInsuerer;

  /**
   * 受取人情報
   */
  ArrayList<BeneficialiesVo> beneficialies;

  /**
   * 保険金・給付金請求資料
   */
  ArrayList<ClaimDocumentsVo> claimDocuments;

  /**
   * 保険金・給付金請求詳細
   */
  List<ClaimDetailsVo> claimDetails;

  /**
   * ロック用
   */
  @JsonIgnore
  Integer claimDetailsUpdateCount;

  public ClaimHeadersVo() {
    beneficialies = new ArrayList<BeneficialiesVo>();
    claimDocuments = new ArrayList<ClaimDocumentsVo>();
    claimDetails = new ArrayList<ClaimDetailsVo>();
  }
}
