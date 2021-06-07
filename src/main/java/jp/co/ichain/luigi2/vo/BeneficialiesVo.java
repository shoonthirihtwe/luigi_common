package jp.co.ichain.luigi2.vo;

import java.util.Date;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jp.co.ichain.luigi2.validity.FormatList;
import jp.co.ichain.luigi2.validity.VoFieldInfo;
import jp.co.ichain.luigi2.validity.VoFieldInfo.Validity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * BeneficialiesVo
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
public class BeneficialiesVo extends ObjectVo {

  @FormatList(list = {"BD", "BM", "PH", "IN", "CM"})
  @VoFieldInfo(name = "役割タイプ")
  String roleType;

  @FormatList(list = {"1", "2"})
  @VoFieldInfo(name = "法人/個人　区分")
  String personType;

  @Size(max = 64)
  @VoFieldInfo(name = "氏名　姓　カナ(会社名)", validitys = {Validity.Size})
  String nameKanaSei;

  @Size(max = 64)
  @VoFieldInfo(name = "氏名　名　カナ", validitys = {Validity.Size})
  String nameKanaMei;

  @Size(max = 64)
  @VoFieldInfo(name = "氏名　姓（会社名）", validitys = {Validity.Size})
  String nameKanjiSei;

  @Size(max = 64)
  @VoFieldInfo(name = "氏名　名", validitys = {Validity.Size})
  String nameKanjiMei;

  @Size(max = 3)
  @VoFieldInfo(name = "受取の割合", validitys = {Validity.Size})
  String share;

  @FormatList(
      list = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "90", "99"})
  @VoFieldInfo(name = "契約者との続柄")
  String relationship;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
