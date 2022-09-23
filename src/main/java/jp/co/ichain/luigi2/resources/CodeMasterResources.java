package jp.co.ichain.luigi2.resources;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
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

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  private CodeMasterResources self;
  private final ApplicationContext applicationContext;

  @Autowired
  TenantResources tenantResources;

  CodeMasterResources(ApplicationContext applicationContext,
      ServiceInstancesBaseResources serviceInstancesBaseResources) {
    this.applicationContext = applicationContext;
    this.serviceInstancesBaseResources = serviceInstancesBaseResources;
  }

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
  @EventListener(ApplicationReadyEvent.class)
  public void initialize() throws JsonMappingException, JsonProcessingException {

    self = applicationContext.getBean(CodeMasterResources.class);
    for (val tenant : tenantResources.getAll()) {
      self.initialize(tenant.getId());
    }
  }

  /**
   * テナントの初期化
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-13
   * @updatedAt : 2021-08-13
   * @param tenantId
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public void initialize(Integer tenantId) throws JsonMappingException, JsonProcessingException {
    self.get(tenantId);
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

    return self.get(tenantId).get(key);
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
  @Cacheable(key = "{ #tenantId }", value = "CodeMasterResources::get")
  public Map<String, List<CodeMasterVo>> get(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {

    ObjectMapper objMapper = new ObjectMapper();
    val codeList = serviceInstancesBaseResources.get(tenantId, "code_master");
    Map<String, List<CodeMasterVo>> codeMap = objMapper.readValue(codeList.get(0).getInherentJson(),
        new TypeReference<Map<String, List<CodeMasterVo>>>() {});
    return codeMap;
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

    return (updatedAt != null && updatedAt >= self.getLastUpdatedAt(tenantId).getTime() ? null
        : self.get(tenantId));
  }

  /**
   * 情報取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2022-03-17
   * @updatedAt : 2022-03-17
   * @param tenantId
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public Date getLastUpdatedAt(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {

    val codeList = serviceInstancesBaseResources.get(tenantId, "code_master");
    var updatedAt = codeList.get(0).getUpdatedAt();
    if (updatedAt == null) {
      updatedAt = codeList.get(0).getCreatedAt();
    }
    return updatedAt;
  }

  /**
   * コードValue取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-01
   * @updatedAt : 2021-10-01
   * @param tenantId
   * @param key
   * @param codeValue
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  public String getValue(Integer tenantId, String key, String codeName)
      throws JsonMappingException, JsonProcessingException {

    val list = self.get(tenantId).get(key);
    if (list != null && codeName != null) {
      Optional<CodeMasterVo> resultOptional =
          list.stream().filter(vo -> codeName.equals(vo.getCodeName()))
              .collect(Collectors.reducing((a, b) -> null));
      if (resultOptional.isPresent()) {
        return resultOptional.get().getCodeValue();
      }
    }
    return null;
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

    val list = self.get(tenantId).get(key);
    if (list != null && codeValue != null) {
      val codeOptional = list.stream().filter(vo -> codeValue.equals(vo.getCodeValue()))
          .collect(Collectors.reducing((a, b) -> null));
      if (codeOptional.isPresent()) {
        return codeOptional.get().getCodeName();
      }
    }
    return null;
  }
}
