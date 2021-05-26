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
 * TenantVO
 *
 * @author : [AOT] g.kim
 *
 * @createdAt : 2021-04-06
 * @updatedAt : 2021-04-06
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TenantVo extends ObjectVo {

  Integer id;

  String tenantName;

  String smsName;

  String domain;

  String userPoolId;

  String appClientId;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  Integer createdUser;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  Integer updatedUser;
}
