package jp.co.ichain.luigi2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ClaimContractSearchVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ClaimContractSearchVo extends ObjectVo {
  /**
   * 保険会社情報コード連番
   */
  String insurerCodeSeq;

  /**
   * 保険会社情報コード開始日
   */
  long insurerInceptionDate;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 保障内容Vo
   */
  RiskHeadersVo benefits;
}
