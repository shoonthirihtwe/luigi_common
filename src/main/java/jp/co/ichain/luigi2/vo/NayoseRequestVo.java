package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NayoseRequestVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-04
 * @updatedAt : 2021-08-04
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class NayoseRequestVo extends ObjectVo {
  /**
   * テナントID
   */
  Integer tenantId;

  /**
   * 氏名セイ(カナ)
   */
  String nameKanaSei;

  /**
   * 氏名メイ(カナ)
   */
  String nameKanaMei;

  /**
   * 生年月日
   */
  Date dateOfBirth;

  /**
   * 性別
   */
  String sex;
  
  /**
   * 法人/個人区分
   */
  String corporateIndividualFlag;

  /**
   * 住所(郵便番号)
   */
  String postalCode;

 
}
