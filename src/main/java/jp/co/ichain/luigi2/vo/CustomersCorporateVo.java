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
public class CustomersCorporateVo extends ObjectVo {

  Integer id;
  
  Integer tenantId;
  
  String customerId;
  
  String corpNameKana;
  
  String corpNameOfficial;
  
  String corpAddrZipCode;
  
  String corpAddrKanaPref;
  
  String corpAddrKana1;
  
  String corpAddrKana2;
  
  String corpAddrKnjPref;
  
  String corpAddrKnj1;
  
  String corpAddrKnj2;
  
  String rep10eSex;
  
  @JsonSerialize(using = JsonDateSerializer.class)
  Date rep10eDateOfBirth;
  
  String rep10eNameKanaSei;
  
  String rep10eNameKanaMei;
  
  String rep10eNameKnjSei;
  
  String rep10eNameKnjMei;
  
  String rep10eAddrZipCode;
  
  String rep10eAddrKanaPref;
  
  String rep10eAddrKana1;
  
  String rep10eAddrKana2;
  
  String rep10eAddrKnjPref;
  
  String rep10eAddrKnj1;
  
  String rep10eAddrKnj2;
  
  String rep10eAddrTel1;
  
  String rep10eAddrTel2;
  
  String contactNameKanaSei;
  
  String contactNameKanaMei;
  
  String contactNameKnjSei;
  
  String contactNameKnjMei;
  
  String contactAddrZipCode;
  
  String contactAddrKanaPref;
  
  String contactAddrKana1;
  
  String contactAddrKana2;
  
  String contactAddrKnjPref;
  
  String contactAddrKnj1;
  
  String contactAddrKnj2;
  
  String contactAddrTel1;
  
  String contactAddrTel2;
  
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
