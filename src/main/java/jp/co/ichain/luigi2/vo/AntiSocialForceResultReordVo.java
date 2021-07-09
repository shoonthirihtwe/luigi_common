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
  String RecordNumber;
  String Delivered;
  String Publication;
  String Name1;
  String Name1Kana;
  String Name2;
  String Name2Kana;
  Integer Age;
  String Gender;
  String Address;
  String Property;
  String Charge;
}
