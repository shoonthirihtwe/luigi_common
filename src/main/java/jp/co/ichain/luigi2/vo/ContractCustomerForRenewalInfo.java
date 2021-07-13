package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ContractCustomerForRenewalInfo
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-07-08
 * @updatedAt : 2021-07-08
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ContractCustomerForRenewalInfo extends ObjectVo {
  /**
   * 証券番号
   */
  String contractNo;

  /**
   * 証券番号枝番
   */
  String contractBranchNo;

  /**
   * 契約ステータス
   */
  String contractStatus;

  /**
   * 現契約日
   */
  Date issueDate;

  /**
   * 満期日
   */
  Date expirationDate;

  /**
   * 販売プランコード
   */
  String salesPlanCode;

  /**
   * 販売プランコード
   */
  String salesPlanTypeCode;

  /**
   * 合計保険料
   */
  Integer totalPremium;

  /**
   * 契約者ID
   */
  String contractorCustomerId;

  /**
   * 被保険者ID
   */
  String insuredCustomerId;

  /**
   * email
   */
  String email;

  /**
   * 契約者 生年月日
   */
  Date dateOfBirth;

  /**
   * 氏名漢字 姓
   */
  String nameKnjSei;

  /**
   * 氏名漢字 名
   */
  String nameKnjMei;

  /**
   * 住所カナ 県
   */
  String addrKnjPref;

  /**
   * 住所漢字 1
   */
  String addrKnj1;

  /**
   * 住所漢字 2
   */
  String addrKnj2;

  /**
   * insEmail
   */
  String insEmail;

  /**
   * 契約者 生年月日
   */
  Date insDateOfBirth;
  /**
   * 氏名漢字 姓
   */
  String insNameKnjSei;

  /**
   * 氏名漢字 名
   */
  String insNameKnjMei;

  /**
   * 住所カナ 県
   */
  String insAddrKnjPref;

  /**
   * 住所漢字 1
   */
  String insAddrKnj1;

  /**
   * 住所漢字 2
   */
  String insAddrKnj2;
}
