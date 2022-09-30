package jp.co.ichain.luigi2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SumUpCheckResultVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2022-09-30
 * @updatedAt : 2022-09-30
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SumUpCheckResultVo extends ObjectVo {

  Boolean benefitGroupTypeBlResult;

  Boolean benefitGroupTypeBylawResult;

  String targetType;
}
