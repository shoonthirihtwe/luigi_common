package jp.co.ichain.luigi2.vo;

import java.util.Date;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.validity.VoFieldInfo;
import jp.co.ichain.luigi2.validity.VoFieldInfo.Validity;
import jp.co.ichain.luigi2.web.config.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 管理者アカウントVo
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UsersVo extends ObjectVo {

  @VoFieldInfo(name = "ID")
  Integer id;

  @VoFieldInfo(name = "テナントID")
  Integer tenantId;

  @Size(max = 32)
  @VoFieldInfo(name = "ユーザ名", validitys = {Validity.ByteSize})
  String name;

  @Size(max = 128)
  @VoFieldInfo(name = "メールアドレス", validitys = {Validity.Email, Validity.Size})
  String email;

  @Size(max = 32)
  @VoFieldInfo(name = "パスワード", validitys = {Validity.Size})
  String password;

  @VoFieldInfo(name = "RememberMe用トークン")
  String rememberToken;

  @VoFieldInfo(name = "最終ログイン日時")
  @JsonSerialize(using = JsonDateSerializer.class)
  Date lastLoginAt;

  @VoFieldInfo(name = "Active/Inactive")
  Boolean active;

  @VoFieldInfo(name = "ロック用")
  Integer updateCount;

  @VoFieldInfo(name = "作成日時")
  @JsonIgnore
  Date createdAt;

  @VoFieldInfo(name = "作成者")
  @JsonIgnore
  String createdBy;

  @VoFieldInfo(name = "最終更新日時")
  @JsonIgnore
  Date updatedAt;

  @VoFieldInfo(name = "最終更新者")
  @JsonIgnore
  String updatedBy;
}
