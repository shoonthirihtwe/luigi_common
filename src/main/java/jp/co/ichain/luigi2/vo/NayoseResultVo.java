package jp.co.ichain.luigi2.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NayoseResultVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-04
 * @updatedAt : 2021-08-04
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class NayoseResultVo extends ObjectVo {
  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 完全一致(顧客ID)
   */
  List<String> matchedList;

  /**
   * 部分一致(顧客ID)
   */
  List<String> partialMatchedList;

  @JsonIgnore
  String customerId;
  
  @JsonIgnore
  String authenticationKey;
}
