package jp.co.ichain.luigi2.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.ichain.luigi2.config.web.JsonDateSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * DownloadFileVo
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class DownloadFileVo extends ObjectVo {

  String key;

  String fileName;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date createdAt;

  @JsonSerialize(using = JsonDateSerializer.class)
  Date lastModified;

  public DownloadFileVo(String key, String fileName, Date createdAt, Date lastModifiedDate) {
    this.key = key;
    this.fileName = fileName;
    this.createdAt = createdAt;
    this.lastModified = lastModifiedDate;
  }
}
