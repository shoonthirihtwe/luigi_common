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
 * InsuredVo ContractRequestVoのした階層
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
public class InsuredVo extends ObjectVo {

  @Size(max = 1)
  @VoFieldInfo(name = "被保険者・契約者と同じ（氏名・生年月日・性別）")
  String sameAsName;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者・氏名カナ　姓", validitys = {Validity.Size})
  String nameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者・氏名カナ　名", validitys = {Validity.Size})
  String nameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者・氏名漢字　姓", validitys = {Validity.Size})
  String nameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者・氏名漢字　名", validitys = {Validity.Size})
  String nameKanjiMei;

  @VoFieldInfo(name = "被保険者・生年月日")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date dateOfBirth;

  @FormatList(list = {"1", "2"})
  @VoFieldInfo(name = "被保険者・性別")
  String sex;

  @FormatList(
      list = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "90", "99"})
  @VoFieldInfo(name = "被保険者・性別")
  String relationship;

  @Size(max = 1)
  @VoFieldInfo(name = "被保険者・契約者と同じ（住所）")
  String sameAsAddr;
  
  @Size(min = 7, max = 7)
  @VoFieldInfo(name = "被保険者・郵便番号", validitys = {Validity.Size})
  String addrZipNo;

  @Size(max = 4)
  @VoFieldInfo(name = "被保険者・住所漢字 県", validitys = {Validity.Size})
  String addrKanaPref;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者・住所漢字１", validitys = {Validity.Size})
  String addrkanji1;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者・住所漢字２", validitys = {Validity.Size})
  String addrKanji2;

  @Size(min = 10, max = 13)
  @VoFieldInfo(name = "被保険者・電話番号1", validitys = {Validity.Tel})
  String inAddrTel1;

  @Size(min = 10, max = 13)
  @VoFieldInfo(name = "被保険者・電話番号2", validitys = {Validity.Tel})
  String inAddrTel2;

  @Size(max = 32)
  @VoFieldInfo(name = "被保険者・職業", validitys = {Validity.Size})
  String inOccupation;

  @Size(max = 32)
  @VoFieldInfo(name = "被保険者・勤務先名カナ", validitys = {Validity.Size})
  String inCompanyNameKana;

  @Size(max = 32)
  @VoFieldInfo(name = "被保険者・勤務先名", validitys = {Validity.Size})
  String inCompanyNameKanji;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・代表者氏名カナ姓", validitys = {Validity.Size})
  String repNameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・代表者氏名カナ 名", validitys = {Validity.Size})
  String repNameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・代表者氏名漢字 姓", validitys = {Validity.Size})
  String repNameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・代表者氏名漢字 名", validitys = {Validity.Size})
  String repNameKanjiMei;

  @Size(min = 7, max = 7)
  @VoFieldInfo(name = "被保険者法人・代表者郵便番号", validitys = {Validity.Size})
  String repAddrZipNo;

  @Size(min = 3, max = 4)
  @VoFieldInfo(name = "被保険者法人・代表者住所漢字　県", validitys = {Validity.Size})
  String repAddrKanjiPref;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・代表者住所漢字 1", validitys = {Validity.Size})
  String repAddrKanji1;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・代表者住所漢字 2", validitys = {Validity.Size})
  String repAddrKanji2;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・担当者氏名カナ姓", validitys = {Validity.Size})
  String staffNameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・担当者氏名カナ 名", validitys = {Validity.Size})
  String staffNameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・担当者氏名漢字 姓", validitys = {Validity.Size})
  String staffNameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・担当者氏名漢字 名", validitys = {Validity.Size})
  String staffNameKanjiMei;

  @Size(min = 7, max = 7)
  @VoFieldInfo(name = "被保険者法人・担当者郵便番号", validitys = {Validity.Size})
  String staffAddrZipNo;

  @Size(min = 3, max = 4)
  @VoFieldInfo(name = "被保険者法人・担当者住所漢字　県", validitys = {Validity.Size})
  String staffAddrKanjiPref;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・担当者住所漢字 1", validitys = {Validity.Size})
  String staffAddrKanji1;

  @Size(max = 64)
  @VoFieldInfo(name = "被保険者法人・担当者住所漢字 2", validitys = {Validity.Size})
  String staffAddrKanji2;

  @Size(min = 10, max = 13)
  @VoFieldInfo(name = "被保険者法人・担当者電話番号", validitys = {Validity.Tel})
  String staffAddrTel1;

  @Size(max = 128)
  @VoFieldInfo(name = "被保険者法人・担当メールアドレス", validitys = {Validity.Email, Validity.Size})
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
