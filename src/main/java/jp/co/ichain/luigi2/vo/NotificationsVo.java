package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NotificationsVo
 *
 * @author : [VJP] n.huy.hoang
 * @createdAt : 2021-06-23
 * @updatedAt : 2021-06-23
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsVo extends ObjectVo {

  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 通知予定日
   */
  Long notificationDate;
  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 前証券番号枝番
   */
  String contractBranchNo;

  /**
   * テンプレートナンバー
   */
  String templateNunber;

  /**
   * 通知実施
   */
  String notificationImplementation;

  /**
   * 通信欄コメント
   */
  String comment;

  /**
   * 通知対象者
   */
  String sendee;

  /**
   * 通知方法
   */
  String notificationMethod;

  /**
   * 通知対象・Eメールアドレス
   */
  String email;

  /**
   * エラーフラグ
   */
  String errorFlag;

  String data;

  Integer updateCount;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;

  @JsonIgnore
  Date deletedAt;

  @JsonIgnore
  String deletedBy;

}
