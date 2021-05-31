package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * PolicyVo
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
public class PolicyVo extends ObjectVo {

  String effectiveDate;

  String applicationDate;

  String receivedDate;

  String inceptionDate;

  String nameKanaSei;

  String nameKanaMei;

  String nameKanjiSei;

  String nameKanjiMei;

  String addrZipNo;

  String addrKanjiPref;

  String addrKanji1;

  String addrKanji2;

  String addrTel1;

  String addrTel2;

  String dateOfBirth;

  String sex;

  String emailAddress;

  String occupation;

  String companyNameKana;

  String companyNameKanji;

  String placeOfWorkKana;

  String placeOfWorkKanji;

  String placeOfWorkCode;

  String groupColumn;

  String materialRepresentation;

  String understandingIntent;

  String confirmApplication;

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
