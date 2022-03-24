package jp.co.ichain.luigi2.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CodeMasterVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-06-23
 * @updatedAt : 2021-06-23
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class CodeMasterVo extends ObjectVo implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @JsonIgnore
  Integer id;

  @JsonIgnore
  Integer tenantId;

  @JsonIgnore
  String field;

  @JsonIgnore
  String tbl;

  String codeValue;

  String codeName;

  Integer updateCount;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  String createdBy;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String updatedBy;

  @JsonIgnore
  Date deletedAt;

  @JsonIgnore
  String deletedBy;
}
