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
 * InsuranceCompanyVo
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
public class InsuranceCompanyVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 保険会社情報コード連番
   */
  String insurerCodeSeq;

  /**
   * 保険会社情報コード開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date insurerInceptionDate;

  /**
   * 保険会社情報コード終了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date insurerTerminationDate;

  /**
   * 保険金支払用 振込依頼人コード
   */
  String transferRequesterCode;

  /**
   * 保険金支払用 振込依頼人
   */
  String transferRequesterName;

  /**
   * 保険金支払用 金融機関コード
   */
  String bankCode;

  /**
   * 保険金支払用 金融機関名
   */
  String bankName;

  /**
   * 保険金支払用 支店コード
   */
  String bankBranchCode;

  /**
   * 保険金支払用 支店名
   */
  String bankBranchName;

  /**
   * 保険金支払用 預金種目
   */
  String bankAccountType;

  /**
   * 保険金支払用 口座番号
   */
  String bankAccountNo;

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
