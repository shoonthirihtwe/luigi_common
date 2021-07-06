package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ContractRevocation
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-06-23
 * @updatedAt : 2021-06-23
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ContractRevocation extends ObjectVo {

  /**
   * 証券番号ID
   */
  Integer contractId;

  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 保険料収納月
   */
  String premiumBillingPeriod;

  /**
   * 保険料 連番
   */
  String premiumSequenceNo;

  /**
   * 現契約日
   */
  Date issueDate;

  /**
   * 氏名漢字 姓
   */
  String nameKnjSei;

  /**
   * 氏名漢字 名
   */
  String nameKnjMei;

  /**
   * 通信先・Eメールアドレス
   */
  String email;
}
