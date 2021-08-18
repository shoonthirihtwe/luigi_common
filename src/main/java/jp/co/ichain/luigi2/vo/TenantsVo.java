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
 * TenantsVo
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
public class TenantsVo extends ObjectVo {

  /**
   * ID
   */
  Integer id;
  /**
   * テナント名
   */
  String tenantName;
  /**
   * サイトURL
   */
  String siteUrl;
  /**
   * 申込時リダクレクトURL
   */
  String entryRedirectUrl;
  /**
   * バッチ日付
   */
  Date batchDate;
  /**
   * オンライン日付
   */
  Date onlineDate;
  /**
   * 補償グループ種別
   */
  String compensationGroupCode;
  /**
   * userPoolId
   */
  String userPoolId;

  @JsonIgnore
  Date createdAt;

  @JsonIgnore
  Date updatedAt;

  @JsonIgnore
  String deletedAt;
}
