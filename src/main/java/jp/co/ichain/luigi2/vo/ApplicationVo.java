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
public class ApplicationVo extends ObjectVo {

  @Size(min = 12, max = 12)
  @VoFieldInfo(name = "保険会社情報コード連番", validitys = {Validity.Size})
  String insurerCodeSeq;

  @Size(min = 10, max = 10)
  @VoFieldInfo(name = "保険会社情報コード開始日", validitys = {Validity.Size})
  String insurerInceptionDate;

  @Size(min = 1, max = 1)
  @VoFieldInfo(name = "証券番号採番", validitys = {Validity.Size})
  String contNumberNotNumbering;

  @FormatList(list = {"0", "1", "2", "12"})
  @VoFieldInfo(name = "払込回数")
  String frequency;

  @FormatList(list = {"1", "2", "3", "4", "5"})
  @VoFieldInfo(name = "払込経路")
  String paymentMethod;

  AgencyInfoVo agencyInfo;

  PolicyInfoVo policy;

  InsuredVo insured;

  BeneficialiesVo beneficiaries;

  String data;

  Integer tenantId;

  String customerId;

  String contractNo;

  String contractBranchNo;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
