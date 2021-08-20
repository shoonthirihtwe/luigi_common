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
 * 保全顧客情報
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-30
 * @updatedAt : 2021-07-30
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestsCustomersVo extends ObjectVo {

  /**
   * 顧客ID
   */
  String customerId;

  /**
   * 契約者個人法人区分
   */
  String corporateIndividualFlag;

  /**
   * 「個人」氏名(漢字)
   */
  String nameKnj;

  /**
   * 「個人」姓
   */
  String nameKnjSei;

  /**
   * 「個人」名
   */
  String nameKnjMei;

  /**
   * 「個人」氏名(カナ)
   */
  String nameKana;

  /**
   * 「個人」セイ
   */
  String nameKanaSei;

  /**
   * 「個人」メイ
   */
  String nameKanaMei;

  /**
   * 「個人」性別
   */
  String sex;

  /**
   * 「個人」生年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date dateOfBirth;

  /**
   * 「個人」住所(郵便番号)
   */
  String addrZipCode;

  /**
   * 「個人」住所(都道府県)
   */
  String addrKnjPref;

  /**
   * 「個人」住所1
   */
  String addrKnj1;

  /**
   * 「個人」住所2
   */
  String addrKnj2;

  /**
   * 「個人」電話番号1
   */
  String addrTel1;

  /**
   * 「個人」電話番号2
   */
  String addrTel2;

  /**
   * 「個人」メールアドレス
   */
  String email;

  /**
   * 「個人」被保険者との続柄
   */
  String relationship;

  /**
   * 「個人」成年後見人
   */
  String guardianCustomerId;

  /**
   * 「法人」法人名(漢字)
   */
  String corpNameOfficial;

  /**
   * 「法人」法人名(カナ)
   */
  String corpNameKana;

  /**
   * 「法人」法人の住所(郵便番号)
   */
  String corpAddrZipCode;

  /**
   * 「法人」法人の住所(都道府県)
   */
  String corpAddrKnjPref;

  /**
   * 「法人」法人の住所1
   */
  String corpAddrKnj1;

  /**
   * 「法人」法人の住所2
   */
  String corpAddrKnj2;

  /**
   * 「法人」代表者の氏名(漢字) 姓
   */
  String rep10eNameKnjSei;

  /**
   * 「法人」代表者の氏名(漢字) 名
   */
  String rep10eNameKnjMei;

  /**
   * 「法人」代表者の氏名(カナ) セイ
   */
  String rep10eNameKanaSei;

  /**
   * 「法人」代表者の氏名(カナ) メイ
   */
  String rep10eNameKanaMei;

  /**
   * 「法人」代表者の生年月日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date rep10eDateOfBirth;

  /**
   * 「法人」代表者の性別
   */
  String rep10eSex;

  /**
   * 「法人」代表者の住所(郵便番号)
   */
  String rep10eAddrZipCode;

  /**
   * 「法人」「法人」代表者の住所(都道府県)
   */
  String rep10eAddrKnjPref;

  /**
   * 「法人」代表者の住所1
   */
  String rep10eAddrKnj1;

  /**
   * 「法人」代表者の住所2
   */
  String rep10eAddrKnj2;

  /**
   * 「法人」代表者の電話番号1
   */
  String rep10eAddrTel1;

  /**
   * 「法人」代表者の電話番号2
   */
  String rep10eAddrTel2;

  /**
   * 「法人」担当者の氏名(漢字) 姓
   */
  String contactNameKnjSei;

  /**
   * 「法人」担当者の氏名(漢字) 名
   */
  String contactNameKnjMei;

  /**
   * 「法人」担当者の氏名(カナ) セイ
   */
  String contactNameKanaSei;

  /**
   * 「法人」担当者の氏名(カナ) メイ
   */
  String contactNameKanaMei;

  /**
   * 「法人」通信先の住所(郵便番号)
   */
  String contactAddrZipCode;

  /**
   * 「法人」通信先の住所(都道府県)
   */
  String contactAddrKnjPref;

  /**
   * 「法人」通信先の住所1
   */
  String contactAddrKnj1;

  /**
   * 「法人」通信先の住所2
   */
  String contactAddrKnj2;

  /**
   * 「法人」通信先の電話番号1
   */
  String contactAddrTel1;

  /**
   * 「法人」通信先の電話番号2
   */
  String contactAddrTel2;

  /**
   * 「法人」通信先のメールアドレス
   */
  String contactEmail;

  Integer updateCount;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;

  @JsonIgnore
  Date deletedAt;

  @JsonIgnore
  String deletedBy;
}
