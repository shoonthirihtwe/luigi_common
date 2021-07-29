package jp.co.ichain.luigi2.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ichain.luigi2.vo.CodeMasterVo;
import lombok.val;

/**
 * CodeMasterリソース
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-06-23
 * @updatedAt : 2021-06-23
 */
@Singleton
@Service
public class CodeMasterResources {

  private Map<Integer, Map<String, List<CodeMasterVo>>> map = null;

  private Map<Integer, Date> updatedAtMap = null;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

  /**
   * 初期化する
   *
   * @author : [AOT] g.kim
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @createdAt : 2021-06-23
   * @updatedAt : 2021-06-23
   */
  @Lock(LockType.WRITE)
  @PostConstruct
  public void initialize() throws JsonMappingException, JsonProcessingException {
    this.map = new HashMap<Integer, Map<String, List<CodeMasterVo>>>();
    this.updatedAtMap = new HashMap<Integer, Date>();

    ObjectMapper objMapper = new ObjectMapper();
    for (val tenantId : serviceInstancesResources.getTenantList()) {

      val codeList = serviceInstancesResources.get(tenantId, "code_master");
      Map<String, List<CodeMasterVo>> codeMap =
          objMapper.readValue(codeList.get(0).getInherentJson(),
              new TypeReference<Map<String, List<CodeMasterVo>>>() {});
      this.map.put(tenantId, codeMap);

      // 日付登録
      var updatedAt = codeList.get(0).getUpdatedAt();
      if (updatedAt == null) {
        updatedAt = codeList.get(0).getCreatedAt();
      }
      updatedAtMap.put(tenantId, updatedAt);
    }

  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public List<CodeMasterVo> get(Integer tenantId, String key)
      throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId).get(key);
  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Map<String, List<CodeMasterVo>> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId);
  }

  /**
   * 情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @param vo
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Map<String, List<CodeMasterVo>> get(Integer tenantId, Long updatedAt)
      throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return (updatedAt != null && updatedAt <= this.updatedAtMap.get(tenantId).getTime()) ? null
        : this.map.get(tenantId);
  }

  /**
   * 全項目取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-07
   * @updatedAt : 2021-05-07
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Set<Integer> getTenantList() throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return this.map.keySet();
  }
}
