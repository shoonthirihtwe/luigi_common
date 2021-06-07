package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jp.co.ichain.luigi2.validity.VoFieldInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * AgencyInfoVo
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
public class AgencyInfoVo extends ObjectVo {

  @VoFieldInfo(name = "総代理店フラグ")
  String generalAgentFlag;

  @VoFieldInfo(name = "代理店コード")
  String[] agencyCode1;

  @VoFieldInfo(name = "募集人コード")
  String agenctCode1;

  @VoFieldInfo(name = "募集割合")
  String agentShare1;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
