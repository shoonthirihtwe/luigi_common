package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 情報
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-10-28
 * @updatedAt : 2021-10-28
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ClaimCustomerVo extends ObjectVo {

  /**
   * 証券番号ID
   */
  Integer contractId;

  /**
   * 氏名漢字 姓
   */
  String nameKnjSei;

  /**
   * 氏名漢字 名
   */
  String nameKnjMei;

  /**
   * 氏名-姓(カナ)
   */
  String nameKanaSei;

  /**
   * 氏名-名(カナ)
   */
  String nameKanaMei;

  /**
   * 生年月日
   */
  Date dateOfBirth;

  /**
   * 住所
   */
  String address;

  /**
   * 電話番号
   */
  String telNo;

  /**
   * 日中連絡先
   */
  String contactTelNo;

  /**
   * メールアドレス
   */
  String email;
}
