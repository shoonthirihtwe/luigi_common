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

  Integer socialSeq;

  Integer tenantId;

  String beneficiaryCode;

  String contractNo;

  String contractBranchNo;

  String roleType;

  String corporateIndividualFlag;

  String nameKanaSei;

  String nameKanaMei;

  String nameKnjSei;

  String nameKnjMei;

  Integer share;

  String relShipToInsured;

  String sex;

  Date dateOfBirth;

  String addrZipCode;

  String addrKnjPref;

  String addrKnj1;

  String addrKnj2;

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

  /**
   * ロック用
   */
  Integer updateCount;

  /**
   * 開始日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date startDate;

  /**
   * 終了日
   */
  @JsonSerialize(using = JsonDateSerializer.class)
  Date endDate;

  /**
   * 契約者との続柄
   */
  String relationship;

  /**
   * 氏名 姓（会社名）
   */
  String nameKanjiSei;

  /**
   * 氏名 名
   */
  String nameKanjiMei;

  /**
   * ロール連番
   */
  Integer roleSequenceNo;

  /**
   * ステータス
   */
  String status;
}
