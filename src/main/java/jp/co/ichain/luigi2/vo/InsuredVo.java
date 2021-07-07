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
 * InsuredVo ApplicationVoのした階層
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

  String sameAsName;

  String nameKanaSei;

  String nameKanaMei;

  String nameKanjiSei;

  String nameKanjiMei;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date dateOfBirth;

  String sex;

  String relationship;

  String sameAsAddr;

  String addrZipNo;

  String addrKanaPref;

  String addrkanji1;

  String addrKanji2;

  String inAddrTel1;

  String inAddrTel2;

  String inOccupation;

  String inCompanyNameKana;

  String inCompanyNameKanji;

  String repNameKanaSei;

  String repNameKanaMei;

  String repNameKanjiSei;

  String repNameKanjiMei;

  String repAddrZipNo;

  String repAddrKanjiPref;

  String repAddrKanji1;

  String repAddrKanji2;

  String staffNameKanaSei;

  String staffNameKanaMei;

  String staffNameKanjiSei;

  String staffNameKanjiMei;

  String staffAddrZipNo;

  String staffAddrKanjiPref;

  String staffAddrKanji1;

  String staffAddrKanji2;

  String staffAddrTel1;

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
