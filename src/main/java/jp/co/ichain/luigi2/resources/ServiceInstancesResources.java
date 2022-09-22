package jp.co.ichain.luigi2.resources;

import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ichain.luigi2.mapper.ServiceInstancesMapper;
import lombok.val;

/**
 * Service Instances リソース
 *
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-07
 * @updatedAt : 2021-05-07
 */
@Singleton
@Service
@DependsOn(value = {"dataSourceInitializer"})
public class ServiceInstancesResources {

  private final ServiceInstancesMapper serviceInstancesMapper;

  ServiceInstancesResources(ServiceInstancesMapper serviceInstancesMapper) {
    this.serviceInstancesMapper = serviceInstancesMapper;
  }

  /**
   * 情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/07
   * @updatedAt : 2022/09/07
   * @param tenantId
   * @param businessGroupType
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @SuppressWarnings("unchecked")
  public Set<String> getSchemaKeys(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    val items = serviceInstancesMapper.selectServiceInstances(tenantId, "schema");

    Set<String> result = null;
    if (items != null && items.size() > 0) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> map = mapper.readValue(items.get(0).getInherentJson(), Map.class);
      result = map.keySet();
    }

    return result;
  }

}
