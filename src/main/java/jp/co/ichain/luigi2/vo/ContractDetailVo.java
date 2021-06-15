package jp.co.ichain.luigi2.vo;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/***
 * ContractDetailVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-06-09
 * @updatedAt : 2021-06-09
 */
@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@AllArgsConstructor
public class ContractDetailVo extends ObjectVo {

  Map<String, Object> productsInfo;

  Map<String, Object> newBusinessDocument;

  Map<String, Object> autoUnderwritingReason;

  Map<String, Object> contractorInfo;

  Map<String, Object> insuredInfo;

  Map<String, Object> paymentInfo;
  
  Map<String, Object> agencyInfo;
  
  Map<String, Object> assessmentInfo1;
  
  Map<String, Object> assessmentInfo2;

  String communicationColumn;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;

  public ContractDetailVo() {
    productsInfo = new ConcurrentHashMap<String, Object>();
    newBusinessDocument = new ConcurrentHashMap<String, Object>();
    autoUnderwritingReason = new ConcurrentHashMap<String, Object>();
    contractorInfo = new ConcurrentHashMap<String, Object>();
    insuredInfo = new ConcurrentHashMap<String, Object>();
    paymentInfo = new ConcurrentHashMap<String, Object>();
  }

}
