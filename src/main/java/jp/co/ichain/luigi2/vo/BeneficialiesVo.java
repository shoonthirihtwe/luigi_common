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

  Integer id;
  
  Integer tenantId;
  
  String beneficiaryCode;
  
  String contractNo;
  
  String contractBranchNo;
  
  String roleType;

  String personType;

  String nameKanaSei;

  String nameKanaMei;

  String nameKnjSei;

  String nameKnjMei;

  String share;

  String relShipToInsured;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
  
  /**
   * ロック用
   */
  Integer updateCount;
}
