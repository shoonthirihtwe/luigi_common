package jp.co.ichain.luigi2.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

    for (val tenantId : serviceInstancesResources.getTenantList()) {
      this.initialize(tenantId);
    }
  }

  /**
   * 初期化する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-13
   * @updatedAt : 2021-08-13
   * @param tenantId
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public void initialize(Integer tenantId) throws JsonMappingException, JsonProcessingException {
    ObjectMapper objMapper = new ObjectMapper();
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
   * コード名取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-09-17
   * @updatedAt : 2021-09-17
   * @param tenantId
   * @param key
   * @param codeValue
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public String getName(Integer tenantId, String key, String codeValue)
      throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }
    val list = this.map.get(tenantId).get(key);
    if (list != null && codeValue != null) {
      return list.stream().filter(vo -> codeValue.equals(vo.getCodeValue()))
          .collect(Collectors.reducing((a, b) -> null)).get().getCodeName();
    }
    return null;
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
