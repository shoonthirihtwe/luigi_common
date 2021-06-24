package jp.co.ichain.luigi2.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 検索情報 DTO
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SearchDto {
  /**
   * ページ番号
   */
  Integer page;

  /**
   * １ページに表示されるItemCount
   */
  Integer rowCount;

  /**
   * ユーザーId
   */
  Integer userId;

  /**
   * 開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDate;

  /**
   * 終了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date endDate;

  /**
   * 始期年月日の開始日（From）
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDateFrom;

  /**
   * 始期年月日の終了日（To）
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDateTo;

  /**
   * 満期年月日の開始日（From）
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date endDateFrom;

  /**
   * 満期年月日の終了日（To）
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date endDateTo;

  /**
   * ソートするカラム名
   */
  String sortColumn = null;

  /**
   * ソートするタイプ
   */
  String sortType = "ASC";
}
