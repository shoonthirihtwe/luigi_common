package jp.co.ichain.luigi2.vo;

import java.util.Date;
import java.util.List;
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
  Integer requestNo;

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
   * 文書
   */
  List<DocumentsVo> documentsList;

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
