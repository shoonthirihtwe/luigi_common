package jp.co.ichain.luigi2.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
import jp.co.ichain.luigi2.vo.CodeInfoVo;
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

  private Map<Integer, Map<String, CodeInfoVo>> map = null;

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
    this.map = new HashMap<Integer, Map<String, CodeInfoVo>>();

    ObjectMapper objMapper = new ObjectMapper();
    for (val tenantId : serviceInstancesResources.getTenantList()) {
      val codeList = serviceInstancesResources.get(tenantId, "code_master");
      val codeInfoVoList = objMapper.readValue(codeList.get(0).getInherentJson(),
          new TypeReference<List<CodeInfoVo>>() {});

      Map<String, CodeInfoVo> keyMap = new ConcurrentHashMap<String, CodeInfoVo>();
      for (val codeInfo : codeInfoVoList) {
        keyMap.put(codeInfo.getKey(), codeInfo);
      }
      this.map.put(tenantId, keyMap);
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
  public CodeInfoVo get(Integer tenantId, String key)
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
  public Map<String, CodeInfoVo> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    return this.map.get(tenantId);
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
