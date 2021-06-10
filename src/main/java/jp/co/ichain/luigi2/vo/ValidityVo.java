package jp.co.ichain.luigi2.vo;

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
public class ValidityVo extends ObjectVo {

  Boolean required = false;

  String type;

  Boolean array = false;

  String objectType;

  Integer min;

  Integer max;

  Object formats;

  Map<String, Object> condition;

  List<Integer> intFixedList;

  List<String> fixedList;
}
