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
 * ServiceObjectsVo
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
public class ServiceObjectsVo extends ObjectVo {

  Integer id;

  Integer tenantId;

  String contractNo;

  String contractBranchNo;

  String data;

  String description;

  Integer level;

  Integer version;

  Integer sequenceNo;
  
  String txType;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  Date deletedAt;

}
