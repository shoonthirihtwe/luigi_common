package jp.co.ichain.luigi2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返却値が一つの場合使うモデル
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-27
 * @updatedAt : 2021-05-27
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResultOneDto<T> extends ResultWebDto {
  /**
   * 返却値
   */
  public T item = null;
  /**
   * アイテムを説明＆親情報
   */
  private Object info = null;

  public ResultOneDto(String code, T item) {
    this.code = code;
    this.item = item;
  }
}
