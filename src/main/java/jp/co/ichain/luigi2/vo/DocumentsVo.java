package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Documents Vo
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
public class DocumentsVo extends ObjectVo {
  /**
   * ID
   */
  Integer id;

  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * キー
   */
  String ownerCode;

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
   * 作成者
   */
  String createdBy;

  /**
   * 最終更新者
   */
  String updatedBy;
}
