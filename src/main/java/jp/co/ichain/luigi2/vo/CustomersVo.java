package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CustomersVo
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
public class CustomersVo extends ObjectVo {

  Integer id;
  
  Integer tenantId;
  
  String customerId;
  
  String contractorFlg;
  
  String insuredFlag;
  
  String nameKanaSei;
  
  String nameKanaMei;
  
  String nameKnjSei;
  
  String nameKnjMei;
  
  String indexName;
  
  String personType;
  
  String sex;
  
  Date dateOfBirth;
  
  String addrZipCode;
  
  String addrKanaPref;
  
  String addrKana1;
  
  String addrKana2;
  
  String addrKnjPref;
  
  String addrKnj1;
  
  String addrKnj2;
  
  String addrTel1;
  
  String addrTel2;
  
  String companyNameKana;
  
  String companyNameKanji;
  
  String placeOfWorkKana;
  
  String placeOfWorkKanji;
  
  String placeOfWorkCode;
  
  String groupColumn;
  
  String email;
  
  String occupation;
  
  String occupationCode;
  
  String insSameAsName;
  
  String insNameKanaSei;
  
  String insNameKanaMei;
  
  String insNameKnjSei;
  
  String insNameKnjMei;
  
  Date insBirthday;
  
  String insSex;
  
  String relationshipToPh;
  
  String insSameAsAddr;
  
  String insAddrZipCode;
  
  String insAddrKanaPref;
  
  String insAddrKana1;
  
  String insAddrKana2;
  
  String insAddrKnjPref;
  
  String insAddrKnj1;
  
  String insAddrKnj2;
  
  String insAddrTel1;
  
  String insAddrTel2;
  
  String insEmail;
  
  String insOccupation;
  
  String insCompanyNameKana;
  
  String insCompanyNameKanji;
  
  String guardianFlag;
  
  String guardianNameKnjSei;
  
  String guardianNameKnjMei;
  
  String guardianNameKanaSei;
  
  String guardianNameKanaMei;
  
  String guardianSex;
  
  Date guardianDateOfBirth;
  
  String guardianType;
  
  String guardianPostalCode;
  
  String guardianAddressPref;
  
  String guardianAddress1;
  
  String guardianAddress2;
  
  String guardianTel1;
  
  String guardianTel2;
  
  String notificationFlag;

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
