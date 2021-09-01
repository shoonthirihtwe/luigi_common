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
 * AgencyBranchesVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-27
 * @updatedAt : 2021-08-27
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AgencyBranchesVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 所属代理店コード
   */

  String agencyCode;

  /**
   * 支店コード
   */
  String agencyBranchCode;

  /**
   * 支店名
   */
  String agencyBranchNameOfficial;

  /**
   * 支店名（カナ）
   */
  String agencyBranchNameKana;

  /**
   * 郵便番号
   */
  String agencyBranchZipCode;

  /**
   * 住所
   */
  String agencyBranchAddress;

  /**
   * 支店電話番号
   */
  String agencyBranchTelNo;

  /**
   * 支店管理者名
   */
  String managerNameKnjSei;

  /**
   * 支店管理者名
   */
  String managerNameKnjMei;

  /**
   * 支店管理者名（カナ）
   */
  String managerNameKanaSei;

  /**
   * 支店管理者名（カナ）
   */
  String managerNameKanaMei;

  /**
   * 担当者名
   */
  String personInChargeKnjSei;

  /**
   * 担当者名
   */
  String personInChargeKnjMei;

  /**
   * 担当者名（カナ）
   */
  String personInChargeKanaSei;

  /**
   * 担当者名（カナ）
   */
  String personInChargeKanaMei;

  /**
   * 担当者電話番号
   */
  String personInChargeTel;

  /**
   * 担当者メールアドレス
   */
  String personInChargeEmail;

  /**
   * 稼働開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDate;

  /**
   * 登録日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date registrationDate;

  /**
   * 廃業日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date endDate;

  /**
   * 支店ステータス
   */
  String agencyBranchStatus;

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
