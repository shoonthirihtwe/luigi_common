package jp.co.ichain.luigi2.vo;

import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gmo Vo
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
// CHECKSTYLE:OFF: checkstyle:AbbreviationAsWordInName
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class GmoPaymentVo extends ObjectVo {

  @JsonProperty(access = Access.WRITE_ONLY)
  String shopID;

  @XmlTransient
  @JsonProperty(access = Access.WRITE_ONLY)
  String shopPass;

  String orderID;

  String jobCd;

  String itemCode;

  Long amount;

  Integer tex;

  Integer tdFlag;

  String tdTenantName;

  String accessID;

  String accessPass;

  Integer method;

  Integer payTimes;

  String token;

  String tranID;

  String siteID;

  String sitePass;

  String memberID;

  String memberName;

  String cardSeq;

  String cardNo;

  String cardName;

  String expire;

  Boolean gmoMemberRegist;

  String targetDate;

  String remarks;

  String checkMode;

  Map<String, PaymentErrorVo> errorMap = null;
}
// CHECKSTYLE:ON: checkstyle:AbbreviationAsWordInName
