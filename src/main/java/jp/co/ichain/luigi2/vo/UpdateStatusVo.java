package jp.co.ichain.luigi2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UpdateStatusVo
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-11
 * @updatedAt : 2021-10-11
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusVo extends ObjectVo {
  /**
   * テナント
   */
  Integer tenantId;
  /**
   * テーブル物理名
   */
  String tableName;
  /**
   * カラム物理名
   */
  String columnName;
  /**
   * 証券番号
   */
  String contractNo;
  /**
   * 枝番（新契約時は必須ではない）
   */
  String contractBranchNo;
  /**
   * 変更後のステータスコード
   */
  String status;
}
