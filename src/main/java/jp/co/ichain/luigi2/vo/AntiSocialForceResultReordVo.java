package jp.co.ichain.luigi2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AntiSocialForceCheckVo
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-07-08
 * @updatedAt : 2021-07-08
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AntiSocialForceResultReordVo {
  String recordNumber;
  String delivered;
  String publication;
  String name1;
  String name1Kana;
  String name2;
  String name2Kana;
  String age;
  String gender;
  String address;
  String property;
  String charge;
}
