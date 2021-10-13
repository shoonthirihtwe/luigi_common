package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ContractPremiumHeader
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-06-24
 * @updatedAt : 2021-06-24
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ContractPremiumHeader extends ObjectVo {

  /**
   * tenantId
   */
  Integer tenantId;
  
  /**
   * 証券番号
   */
  String contractNo;
  
  /**
   * 証券番号枝番
   */
  String contractBranchNo;
  
  /**
   * 現契約日
   */
  Date issueDate;
  
  /**
   * 合計保険料
   */
  Integer totalPremium;
  
  /**
   * 保険料払込回数
   */
  String frequency;
  
  /**
   * batchDate
   */
  Date batchDate;
}
