package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
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
   * 保険期間
   */
  String coverageYear;

  /**
   * 更新後満期日
   */
  String renewalExpirationDate;

  /**
   * 更新前保険料
   */
  Integer premiumAmount;

  /**
   * 更新後保険料
   */
  String renewalPremiumAmount;

  String updateCount;

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
  @JsonSerialize(using = JsonDateSerializer.class)
  Date issueDate;

  /**
   * 満期日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date expirationDate;

  /**
   * 保険者の法人/個人区分
   */
  String corporateIndividualFlag;

  /**
   * 被保険者の法人/個人区分
   */
  String insCorporateIndividualFlag;
  /**
   * 販売プランコード
   */
  String salesPlanCode;

  /**
   * 販売プランコード
   */
  String salesPlanTypeCode;

  /**
   * 画面表示名
   */
  String salesPlanNameDisplay;

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
  @JsonSerialize(using = JsonDateSerializer.class)
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
   * 氏名漢字:個人顧客:姓+名, 法人顧客: corp_name_official
   */
  String nameKnj;

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
   * 住所漢字:
   */
  String address;

  /**
   * insEmail
   */
  String insEmail;

  /**
   * 申込日
   */
  Date applicationDate;

  /**
   * 受付日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  /**
   * 被保険者氏名漢字 姓
   */
  String insNameKnjSei;

  /**
   * 被保険者氏名漢字 名
   */
  String insNameKnjMei;

  /**
   * 氏名漢字:個人顧客:姓+名, 法人顧客: corp_name_official
   */
  String insNameKnj;

  /**
   * 被保険者住所カナ 県
   */
  String insAddrKnjPref;

  /**
   * 被保険者住所漢字 1
   */
  String insAddrKnj1;

  /**
   * 被保険者住所漢字 2
   */
  String insAddrKnj2;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date insDateOfBirth;

  /**
   * 被保険者住所漢字
   */
  String insAddress;
}
