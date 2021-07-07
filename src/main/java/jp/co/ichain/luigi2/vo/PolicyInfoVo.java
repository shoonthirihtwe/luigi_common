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

  @JsonSerialize(using = JsonDateSerializer.class)
  Date effectiveDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date applicationDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date receivedDate;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date inceptionDate;

  String nameKanaSei;

  String nameKanaMei;

  String nameKnjSei;

  String nameKnjMei;

  String addrZipNo;

  String addrKnjPref;

  String addrKnj1;

  String addrKnj2;

  String addrTel1;

  String addrTel2;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date dateOfBirth;

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

  String repNameKnjSei;

  String repNameKnjMei;

  String repAddrZipNo;

  String repAddrKnjPref;

  String repAddrKnj1;

  String repAddrKnj2;
  
  String repAddrTel1;
  
  String repAddrTel2;

  String staffNameKanaSei;

  String staffNameKanaMei;

  String staffNameKnjSei;

  String staffNameKnjMei;

  String staffAddrZipNo;

  String staffAddrKnjPref;

  String staffAddrKnj1;

  String staffAddrKnj2;

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
