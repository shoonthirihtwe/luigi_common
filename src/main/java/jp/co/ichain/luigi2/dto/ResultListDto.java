package jp.co.ichain.luigi2.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返却値がリストの場合使うモデル
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
public class ResultListDto<T> extends ResultOneDto<T> {
  /**
   * ページングに使う総個数
   */
  private Integer totalCount = null;
  /**
   * 取得した情報数
   */
  private Integer count = null;
  /**
   * ページ番号
   */
  private Integer page = null;;
  /**
   * ページ別表示数
   */
  private Integer pageRowCount = null;;

  /**
   * リストを説明＆親情報
   */
  private Object info = null;

  /**
   * 取得情報リスト
   */
  private List<T> items = null;

  /**
   * リストをセットする ・[count]がセットしてないならリスト数でセットする。 ・[totalCount]がセットしてないなら[count]でセットする。
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param list
   */
  public void setItems(List<T> list) {
    this.items = list;
    if (list != null) {
      this.count = list.size();
    }
    if (this.totalCount == null) {
      this.totalCount = this.count;
    }
  }
}
