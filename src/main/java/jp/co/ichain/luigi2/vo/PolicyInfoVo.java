package jp.co.ichain.luigi2.vo;

import java.util.Date;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.validity.FormatList;
import jp.co.ichain.luigi2.validity.VoFieldInfo;
import jp.co.ichain.luigi2.validity.VoFieldInfo.Validity;
import jp.co.ichain.luigi2.web.config.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * PolicyInfoVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PolicyInfoVo extends ObjectVo {

  @VoFieldInfo(name = "責任開始日")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date effectiveDate;

  @VoFieldInfo(name = "申込年月日")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date applicationDate;

  @VoFieldInfo(name = "受付年月日")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  @VoFieldInfo(name = "契約開始日")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date inceptionDate;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者・氏名カナ 姓", validitys = {Validity.Size})
  String nameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者・氏名カナ 名", validitys = {Validity.Size})
  String nameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者・氏名漢字 姓", validitys = {Validity.Size})
  String nameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者・氏名漢字 名", validitys = {Validity.Size})
  String nameKanjiMei;

  @Size(min = 7, max = 7)
  @VoFieldInfo(name = "契約者・郵便番号", validitys = {Validity.Size})
  String addrZipNo;

  @Size(min = 3, max = 4)
  @VoFieldInfo(name = "契約者・郵便番号", validitys = {Validity.Size})
  String addrKanjiPref;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者・住所漢字 1", validitys = {Validity.Size})
  String addrKanji1;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者・住所漢字 2", validitys = {Validity.Size})
  String addrKanji2;

  @Size(min = 10, max = 13)
  @VoFieldInfo(name = "契約者・電話番号1", validitys = {Validity.Tel})
  String addrTel1;

  @Size(min = 10, max = 13)
  @VoFieldInfo(name = "契約者・電話番号2", validitys = {Validity.Tel})
  String addrTel2;

  @VoFieldInfo(name = "契約者・生年月日")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date dateOfBirth;

  @FormatList(list = {"1", "2", "3", "4"})
  @VoFieldInfo(name = "契約者・性別")
  String sex;

  @Size(max = 128)
  @VoFieldInfo(name = "契約者・E メールアドレス", validitys = {Validity.Email, Validity.Size})
  String emailAddress;

  @Size(max = 32)
  @VoFieldInfo(name = "契約者・職業", validitys = {Validity.Size})
  String occupation;

  @Size(max = 32)
  @VoFieldInfo(name = "契約者・勤務先名カナ", validitys = {Validity.Size})
  String companyNameKana;

  @Size(max = 32)
  @VoFieldInfo(name = "契約者・勤務先名", validitys = {Validity.Size})
  String companyNameKanji;

  @Size(max = 25)
  @VoFieldInfo(name = "契約者・勤務先所属名カナ", validitys = {Validity.Size})
  String placeOfWorkKana;

  @Size(max = 25)
  @VoFieldInfo(name = "契約者・勤務先所属名漢字", validitys = {Validity.Size})
  String placeOfWorkKanji;

  @Size(max = 15)
  @VoFieldInfo(name = "契約者・勤務先所属コード", validitys = {Validity.Size})
  String placeOfWorkCode;

  @Size(max = 30)
  @VoFieldInfo(name = "契約者・団体使用欄", validitys = {Validity.Size})
  String groupColumn;

  @FormatList(list = {"1"})
  @VoFieldInfo(name = "契約者・重要事項確認区分")
  String materialRepresentation;

  @FormatList(list = {"1"})
  @VoFieldInfo(name = "契約者・意向確認区分")
  String understandingIntent;

  @FormatList(list = {"1"})
  @VoFieldInfo(name = "契約者・申込内容確認区分")
  String confirmApplication;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・代表者氏名カナ姓", validitys = {Validity.Size})
  String repNameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・代表者氏名カナ 名", validitys = {Validity.Size})
  String repNameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・代表者氏名漢字 姓", validitys = {Validity.Size})
  String repNameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・代表者氏名漢字 名", validitys = {Validity.Size})
  String repNameKanjiMei;

  @Size(min = 7, max = 7)
  @VoFieldInfo(name = "契約者法人・代表者郵便番号", validitys = {Validity.Size})
  String repAddrZipNo;

  @Size(min = 3, max = 4)
  @VoFieldInfo(name = "契約者法人・代表者住所漢字　県", validitys = {Validity.Size})
  String repAddrKanjiPref;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・代表者住所漢字 1", validitys = {Validity.Size})
  String repAddrKanji1;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・代表者住所漢字 2", validitys = {Validity.Size})
  String repAddrKanji2;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・担当者氏名カナ姓", validitys = {Validity.Size})
  String staffNameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・担当者氏名カナ 名", validitys = {Validity.Size})
  String staffNameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・担当者氏名漢字 姓", validitys = {Validity.Size})
  String staffNameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・担当者氏名漢字 名", validitys = {Validity.Size})
  String staffNameKanjiMei;

  @Size(min = 7, max = 7)
  @VoFieldInfo(name = "契約者法人・担当者郵便番号", validitys = {Validity.Size})
  String staffAddrZipNo;

  @Size(min = 1, max = 4)
  @VoFieldInfo(name = "契約者法人・担当者住所漢字　県", validitys = {Validity.Size})
  String staffAddrKanjiPref;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・担当者住所漢字 1", validitys = {Validity.Size})
  String staffAddrKanji1;

  @Size(max = 64)
  @VoFieldInfo(name = "契約者法人・担当者住所漢字 2", validitys = {Validity.Size})
  String staffAddrKanji2;

  @Size(min = 10, max = 13)
  @VoFieldInfo(name = "契約者法人・担当者電話番号", validitys = {Validity.Tel})
  String staffAddrTel1;

  @Size(max = 128)
  @VoFieldInfo(name = "契約者法人・担当メールアドレス", validitys = {Validity.Email})
  String staffEmailAddress;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
