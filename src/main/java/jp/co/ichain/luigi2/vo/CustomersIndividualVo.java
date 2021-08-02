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
public class CustomersIndividualVo extends ObjectVo {

  Integer id;

  Integer tenantId;

  String customerId;

  String nameKanaSei;

  String nameKanaMei;

  String nameKnjSei;

  String nameKnjMei;

  String sex;

  @JsonSerialize(using = JsonDateSerializer.class)
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

  /**
   * 被保険者との続柄
   */
  String relationship;

  /**
   * 成年後見人
   */
  String guardianCustomerId;

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
