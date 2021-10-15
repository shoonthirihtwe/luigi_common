package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSimpleFormatSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PolicyHolderReferenceVo
 *
 * @author : [VJP] タン
 * @createdAt : 2021-07-09
 * @updatedAt : 2021-07-09
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PolicyHolderReferenceVo extends ObjectVo {
  /**
   * 法人/個人 区分
   */
  @JsonIgnore
  String customerType;

  /**
   * 契約者ID
   */
  String contractorCustomerId;

  /**
   * 契約者・１回分保険料
   */
  Integer premium;

  /**
   * 契約者・氏名カナ 姓
   */
  String nameKanaSei;

  /**
   * 契約者・氏名カナ 名
   */
  String nameKanaMei;

  /**
   * 契約者・氏名漢字 姓
   */
  String nameKanjiSei;

  /**
   * 契約者・氏名漢字 名
   */
  String nameKanjiMei;

  /**
   * 契約者・郵便番号
   */
  String addrZipNo;

  /**
   * 契約者・住所カナ 県
   */
  String addrKanaPref;

  /**
   * 契約者・住所カナ１
   */
  String addrKana1;

  /**
   * 契約者・住所カナ２
   */
  String addrKana2;

  /**
   * 契約者・住所漢字 県
   */
  String addrKanjiPref;

  /**
   * 契約者・住所漢字 1
   */
  String addrKanji1;

  /**
   * 契約者・住所漢字 2
   */
  String addrKanji2;

  /**
   * 契約者・生年月日
   */
  @JsonSerialize(using = JsonDateSimpleFormatSerializer.class)
  Date dateOfBirth;

  /**
   * 契約者・性別
   */
  String sex;

  /**
   * 契約者・電話番号１
   */
  String telNo1;

  /**
   * 契約者・電話番号２
   */
  String telNo2;

  /**
   * 契約者・E メールアドレス
   */
  String email;

  /**
   * 契約者・職業
   */
  String occupation;

  /**
   * 契約者・勤務先名カナ
   */
  String companyNameKana;

  /**
   * 契約者・勤務先名
   */
  String companyNameKanji;

  /**
   * 契約者・勤務先所属名カナ
   */
  String placeOfWorkKana;

  /**
   * 契約者・勤務先所属名漢字
   */
  String placeOfWorkKanji;

  /**
   * 契約者・勤務先所属コード
   */
  String placeOfWorkCode;

  /**
   * 契約者・団体使用欄
   */
  String groupColumn;

  /**
   * 契約者法人・代表者氏名カナ姓
   */
  String repNameKanaSei;

  /**
   * 契約者法人・代表者氏名カナ 名
   */
  String repNameKanaMei;

  /**
   * 契約者法人・代表者氏名漢字 姓
   */
  String repNameKanjiSei;

  /**
   * 契約者法人・代表者氏名漢字 名
   */
  String repNameKanjiMei;

  /**
   * 契約者法人・代表者郵便番号
   */
  String repAddrZipNo;

  /**
   * 契約者法人・代表者住所漢字 県
   */
  String repAddrKanjiPref;

  /**
   * 契約者法人・代表者住所漢字 1
   */
  String repAddrKanji1;

  /**
   * 契約者法人・代表者住所漢字 2
   */
  String repAddrKanji2;

  /**
   * 契約者法人・代表者電話番号 1
   */
  String repAddrTel1;

  /**
   * 契約者法人・代表者電話番号 2
   */
  String repAddrTel2;

  /**
   * 契約者法人・担当者氏名カナ姓
   */
  String staffNameKanaSei;

  /**
   * 契約者法人・担当者氏名カナ 名
   */
  String staffNameKanaMei;

  /**
   * 契約者法人・担当者氏名漢字 姓
   */
  String staffNameKanjiSei;

  /**
   * 契約者法人・担当者氏名漢字 名
   */
  String staffNameKanjiMei;

  /**
   * 契約者法人・担当者郵便番号
   */
  String staffAddrZipNo;

  /**
   * 契約者法人・担当者住所漢字 県
   */
  String staffAddrKanjiPref;

  /**
   * 契約者法人・担当者住所漢字 1
   */
  String staffAddrKanji1;

  /**
   * 契約者法人・担当者住所漢字 2
   */
  String staffAddrKanji2;

  /**
   * 契約者法人・担当者電話番号 1
   */
  String staffAddrTel1;

  /**
   * 契約者法人・担当者電話番号 2
   */
  String staffAddrTel2;

  /**
   * 契約者法人・担当メールアドレス
   */
  String staffEmailAddress;
}
