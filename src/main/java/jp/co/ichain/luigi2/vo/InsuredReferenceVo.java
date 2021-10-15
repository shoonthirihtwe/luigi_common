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
 * InsuredReferenceVo
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
public class InsuredReferenceVo extends ObjectVo {
  /**
   * 法人/個人 区分
   */
  @JsonIgnore
  String customerType;

  /**
   * 被保険者ID
   */
  String insuredCustomerId;

  /**
   * 被保険者・氏名カナ 姓
   */
  String inNameKanaSei;

  /**
   * 被保険者・氏名カナ 名
   */
  String inNameKanaMei;


  /**
   * 被保険者・氏名漢字 姓
   */
  String inNameKanjiSei;

  /**
   * 被保険者・氏名漢字 名
   */
  String inNameKanjiMei;

  /**
   * 被保険者・生年月日
   */
  @JsonSerialize(using = JsonDateSimpleFormatSerializer.class)
  Date inDateOfBirth;

  /**
   * 被保険者・性別
   */
  String inSex;

  /**
   * 被保険者・契約者からみた続柄
   */
  String inRelationship;

  /**
   * 被保険者・郵便番号
   */
  String inAddrZipNo;
  /**
   * 被保険者・住所カナ 県
   */
  String inAddrKanaPref;

  /**
   * 被保険者・住所カナ１
   */
  String inAddrKana1;

  /**
   * 被保険者・住所カナ２
   */
  String inAddrKana2;

  /**
   * 被保険者・住所漢字 県
   */
  String inAddrKanjiPref;

  /**
   * 被保険者・住所漢字１
   */
  String inAddrKanji1;

  /**
   * 被保険者・住所漢字２
   */
  String inAddrKanji2;

  /**
   * 被保険者・電話番号1
   */
  String inAddrTel1;

  /**
   * 被保険者・電話番号2
   */
  String inAddrTel2;

  /**
   * 被保険者・E メールアドレス
   */
  String inEmailAddress;

  /**
   * 被保険者・職業
   */
  String inOccupation;

  /**
   * 被保険者・勤務先名カナ
   */
  String inCompanyNameKana;

  /**
   * 被保険者・勤務先名
   */
  String inCompanyNameKanji;

  /**
   * 被保険者・勤務先所属名カナ
   */
  String inPlaceOfWorkKana;

  /**
   * 被保険者・勤務先所属名漢字
   */
  String inPlaceOfWorkKanji;

  /**
   * 被保険者・勤務先所属コード
   */
  String inPlaceOfWorkCode;

  /**
   * 被保険者法人・代表者氏名カナ姓
   */
  String inRepNameKanaSei;

  /**
   * 被保険者法人・代表者氏名カナ 名
   */
  String inRepNameKanaMei;

  /**
   * 被保険者法人・代表者氏名漢字 姓
   */
  String inRepNameKanjiSei;

  /**
   * 被保険者法人・代表者氏名漢字 名
   */
  String inRepNameKanjiMei;

  /**
   * 被保険者法人・代表者郵便番号
   */
  String inRepAddrZipNo;

  /**
   * 被保険者法人・代表者住所漢字 県
   */
  String inRepAddrKanjiPref;

  /**
   * 被保険者法人・代表者住所漢字 1
   */
  String inRepAddrKanji1;

  /**
   * 被保険者法人・代表者住所漢字 2
   */
  String inRepAddrKanji2;

  /**
   * 被保険者法人・代表者電話番号 1
   */
  String inRepAddrTel1;

  /**
   * 被保険者法人・代表者電話番号 2
   */
  String inRepAddrTel2;

  /**
   * 被保険者法人・担当者氏名カナ姓
   */
  String inStaffNameKanaSei;

  /**
   * 被保険者法人・担当者氏名カナ 名
   */
  String inStaffNameKanaMei;

  /**
   * 被保険者法人・担当者氏名漢字 姓
   */
  String inStaffNameKanjiSei;

  /**
   * 被保険者法人・担当者氏名漢字 名
   */
  String inStaffNameKanjiMei;

  /**
   * 被保険者法人・担当者郵便番号
   */
  String inStaffAddrZipNo;

  /**
   * 被保険者法人・担当者住所漢字 県
   */
  String inStaffAddrKanjiPref;

  /**
   * 被保険者法人・担当者住所漢字 1
   */
  String inStaffAddrKanji1;

  /**
   * 被保険者法人・担当者住所漢字 2
   */
  String inStaffAddrKanji2;

  /**
   * 被保険者法人・担当者電話番号 1
   */
  String inStaffAddrTel1;

  /**
   * 被保険者法人・担当者電話番号 2
   */
  String inStaffAddrTel2;

  /**
   * 被保険者法人・担当メールアドレス
   */
  String inStaffEmailAddress;
}
