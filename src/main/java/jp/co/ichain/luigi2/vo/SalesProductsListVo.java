package jp.co.ichain.luigi2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SalesProductsListVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-07-21
 * @updatedAt : 2021-07-21
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SalesProductsListVo extends ObjectVo {
  /**
   * ID
   */
  String id;

  /**
   * 販売プラン名
   */
  String salesPlanName;

  /**
   * 画面表示名
   */
  String salesPlanNameDisplay;
}
