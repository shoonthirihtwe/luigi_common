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
 * ClaimDocumentsVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-23
 * @updatedAt : 2021-06-23
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDocumentsVo extends ObjectVo {


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
   * 連番
   */
  Integer sequenceNo;
  
  /**
   * タイトル
   */
  String documentTitle;
  
  /**
   * URL
   */
  String documentUrl;
  
  /**
   * 登録日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date uploadDate;
  
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
