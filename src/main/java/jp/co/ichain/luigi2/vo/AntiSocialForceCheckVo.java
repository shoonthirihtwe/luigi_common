package jp.co.ichain.luigi2.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AntiSocialForceCheckVo
 *
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-07-08
 * @updatedAt : 2021-07-08
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AntiSocialForceCheckVo {
  String ResultCode;
  String NumberOfRecord;
  List<AntiSocialForceResultReordVo> ResultReords;
  
}
