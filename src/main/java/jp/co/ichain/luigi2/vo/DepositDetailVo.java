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
 * DepositDetailVo
 *
 * @author : [VJP] n.huy.hoang
 * @createdAt : 2021-06-16
 * @updatedAt : 2021-06-16
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DepositDetailVo {
  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 保険料収納処理実施日
   */
  String dueDate;
  /**
   * 未納保険料
   */
  Integer totalPremiumAmount;

  /**
   * 未納分保険料月
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date premiumDueDate;

  /**
   * 保険料充当不可理由取得
   */
  String reasonDontApply;

  /**
   * 猶予期間満了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date gracePeriodExpriration;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;

}
