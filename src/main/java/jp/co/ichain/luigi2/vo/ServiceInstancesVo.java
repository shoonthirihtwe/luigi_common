package jp.co.ichain.luigi2.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ServiceInstancesVo
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
public class ServiceInstancesVo extends ObjectVo implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  Integer id;

  Integer tenantId;

  Integer templateId;

  String businessGroupType;

  String sourceKey;

  String sourceType;

  String inherentJson;

  Map<String, Object> inherentMap;

  List<Object> inherentList;

  String inherentText;

  String description;

  String status;

  Integer version;

  Integer updateCount;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;
}
