package jp.co.ichain.luigi2.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import jp.co.ichain.luigi2.config.web.JsonTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MaintenanceRequestsVo
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-28
 * @updatedAt : 2021-06-28
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestsVo extends ObjectVo {

  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 保全申請番号
   */
  String requestNo;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 契約者名（漢字）
   */
  String contractNameKnj;

  /**
   * 契約者名（カナ）
   */
  String contractNameKana;

  /**
   * 販売プランコード
   */
  String salesPlanCode;

  /**
   * 販売プランタイプコード
   */
  String salesPlanTypeCode;

  /**
   * 現契約日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date issueDate;

  /**
   * 有効/無効フラグ
   */
  String activeInactive;

  /**
   * 保全申請分類
   */
  String transactionCode;

  /**
   * 保全申請分類
   */
  String requestStatus;

  /**
   * 申込日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date applicationDate;

  /**
   * 申込時刻
   */
  @JsonSerialize(using = JsonTimeSerializer.class)
  Date applicationTime;

  /**
   * 申込経路
   */
  String applicationMethod;

  /**
   * 消滅基準日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date terminationBaseDate;

  /**
   * 商品タイプ
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  /**
   * 受付場所
   */
  String receivedAt;

  /**
   * 一次査定コメント
   */
  String commentUnderweiter1;

  /**
   * 一次査定コメント
   */
  String firstAssessmentResults;

  /**
   * 二次査定結果
   */
  String commentUnderweiter2;

  /**
   * 二次査定コメント
   */
  String secondAssessmentResults;

  /**
   * 通信欄
   */
  String communicationColumn;

  /**
   * 払込方法コード
   */
  String paymentMethodCode;

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
   * 口座名義人
   */
  String bankAccountName;

  /**
   * 適用日
   */
  @JsonSerialize(using = JsonTimeSerializer.class)
  Date applyDate;

  /**
   * 完了通知送信日時
   */
  @JsonSerialize(using = JsonTimeSerializer.class)
  Date notificationDatetime;

  /**
   * 処理起票区分
   */
  String entryType;

  /**
   * 契約者メールアドレス
   */
  String contractEmail;

  /**
   * 通知用メールアドレス
   */
  String emailForNotification;

  /**
   * 文書
   */
  List<DocumentsVo> documentsList;

  /**
   * 保険料返金額
   */
  Long refundAmount;

  /**
   * 保険料総返金額
   */
  Long totalRefundAmount;

  /**
   * 解約払戻金額
   */
  Integer cashValue;

  /**
   * 契約者からみた続柄
   */
  String relationship;

  /**
   * 住所変更顧客情報
   */
  MaintenanceRequestsTransferVo transfer;

  /**
   * 受取人
   */
  List<BeneficialiesVo> beneficiariesList;

  /**
   * 固有データ
   */
  List<Map<String, Object>> inherentList;

  /**
   * 決済情報
   */
  String billingInfo;

  /**
   * SBS スマート請求ID
   */
  String smartClaimId;

  /**
   * SBS CIF-ID
   */
  String cifId;

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
