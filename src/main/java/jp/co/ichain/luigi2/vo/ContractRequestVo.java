package jp.co.ichain.luigi2.vo;

import java.util.Date;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jp.co.ichain.luigi2.validity.FormatList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * ContractRequestVo
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
public class ContractRequestVo extends ObjectVo {

  @Size(min = 20, max = 20)
  String insurerCodeSeq;

  @Size(min = 10, max = 10)
  String insurerInceptionDate;

  @Size(min = 1, max = 1)
  String contNumberNotNumbering;

  @Size(min = 1, max = 2)
  String frequency;

  @FormatList(list = {"0", "1", "2", "12"})
  String paymentMethod;

  AgencyInfoVo agencyInfo;

  PolicyVo policy;

  InsuredVo insured;

  BeneficialiesVo beneficialies;

  String data;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
