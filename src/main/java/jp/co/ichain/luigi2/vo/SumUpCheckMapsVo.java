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
public class SumUpCheckMapsVo extends ObjectVo {

  Integer id;
  /**
   * テナントID
   */
  Integer tenantId;
  /**
   * 通算対象者
   */
  String targetType;
  /**
   * 保険金・給付金グループ種別コード
   */
  String benefitGroupType;
  /**
   * 保険金・給付金グループ種別コード
   */
  String benefitGroupTypeName;
  /*
   * 通算上限額
   */
  Integer sumUpAmount;
  /*
   * 開始日
   */
  Date startDate;
  /*
   * 終了日
   */
  Date endDate;
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

  /**
   * ロック用
   */
  Integer updateCount;
}
