package jp.co.ichain.luigi2.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DepositHeadersVo
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
public class DepositHeadersVo extends ObjectVo {
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
  String  receivedAmount;
  
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
  
  /**
   * 詳細入金一覧
   */
  List<DepositDetailsVo> depositDetails;
}
