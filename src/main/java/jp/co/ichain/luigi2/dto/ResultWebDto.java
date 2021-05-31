package jp.co.ichain.luigi2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返却値 DTO
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResultWebDto {

  /**
   * 情報の状態。 成功の場合 [OK] 失敗の場合はエラーコードがセットされ返却される。
   */
  protected String code = null;
}
