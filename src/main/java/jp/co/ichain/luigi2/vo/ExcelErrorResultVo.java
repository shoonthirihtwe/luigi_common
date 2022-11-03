package jp.co.ichain.luigi2.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CsvErrorResultVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-07-01
 * @updatedAt : 2021-07-01
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ExcelErrorResultVo extends ObjectVo {
  /**
   * error lineNumber
   */
  Integer lineNumber;
  
  /**
   * error code
   */
  String code;
  
  /**
   * error args
   */
  List<Object> errArgs;
  
  /**
   * error parentKey
   */
  String parentKey;
  
  /**
   * error arrayIndex
   */
  Integer arrayIndex;

}
