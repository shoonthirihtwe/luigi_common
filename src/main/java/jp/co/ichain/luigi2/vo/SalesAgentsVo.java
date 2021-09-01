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
 * SalesAgentsVo
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
public class SalesAgentsVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 募集人コード
   */
  String agentCode;
  
  /**
   * 所属代理店コード
   */
  String agencyCode;

  /**
   * 協会登録募集人コード
   */
  String offcialAgentCode;

  /**
   * 所属支店コード
   */
  String agencyBranchCode;

  /**
   * 氏名
   */
  String agentNameKnj;

  /**
   * 氏名（カナ）
   */
  String agentNameKana;
  
  /**
   * 合格番号
   */
  String officialAgencyCode;

  /**
   * 生年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date dateOfBirth;
  
  /**
   * 資格有効期限
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date licenseyExpireDate;

  /**
   * 稼働開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDate;

  /**
   * 研修受講日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date trainingDate;
 
  /**
   * 募集人登録日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date registrationDate;

  /**
   * 廃業日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date terminationDate;

  /**
   * 募集人ステータス
   */
  String agentStatus;
  
  /**
   * メモ
   */
  String memo;

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
