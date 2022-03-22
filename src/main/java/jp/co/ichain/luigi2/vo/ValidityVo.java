package jp.co.ichain.luigi2.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 検証用 Vo
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-07
 * @updatedAt : 2021-06-07
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ValidityVo extends ObjectVo implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  Boolean required = false;

  String type;

  Boolean array = false;

  Boolean isBinaryLength = true;

  Boolean isChildrenCondition = true;

  String objectType;

  Integer min;

  Integer max;

  Object formats;

  String codeMaster;

  Map<String, Object> condition;

  String conversion;

  String regex;

  List<Integer> intFixedList;

  List<String> fixedList;
}
