package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
   * スキーマ情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @SuppressWarnings("unchecked")
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getSchema")
  public Map<String, Object> getSchema(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    val item = serviceInstancesMapper.selectServiceInstances(tenantId, "schema");

    Map<String, Object> result = null;
    if (item != null) {
      ObjectMapper mapper = new ObjectMapper();
      result = mapper.readValue(item.getInherentJson(), Map.class);
    }

    return result;
  }
  
  /**
   * スキーマキー取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getSchemaKeys")
  public List<String> getSchemaKeys(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    val schemaMap = getSchema(tenantId);

    Set<String> result = null;
    if (schemaMap != null) {
      result = schemaMap.keySet();
    } else {
      result = new HashSet<String>();
    }

    return new ArrayList<String>(result);
  }

  /**
   * UIテンプレート情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/10/14
   * @updatedAt : 2022/10/14
   * @param tenantId
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */
  @SuppressWarnings("unchecked")
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getUiTemplates")
  public Map<String, Object> getUiTemplates(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    val item = serviceInstancesMapper.selectServiceInstances(tenantId, "ui_template");

    Map<String, Object> result = null;
    if (item != null) {
      ObjectMapper mapper = new ObjectMapper();
      result = mapper.readValue(item.getInherentJson(), Map.class);
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getEnums")
  public List<Map<String, Object>> getEnums(Integer tenantId)
      throws JsonMappingException, JsonProcessingException {
    val items = serviceInstancesMapper.selectServiceInstancesForLike(tenantId, "enum_%");

    List<Map<String, Object>> result = null;
    if (items != null) {
      result = new ArrayList<Map<String, Object>>();

      for (val item : items) {
        ObjectMapper mapper = new ObjectMapper();
        result.add(mapper.readValue(item.getInherentJson(), Map.class));
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Cacheable(key = "{ #tenantId:#field }", value = "ServiceInstancesResources::getEnum")
  public Map<String, Object> getEnum(Integer tenantId, String field)
      throws JsonMappingException, JsonProcessingException {
    val vo = serviceInstancesMapper.selectServiceInstances(tenantId, "enum_" + field);

    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(vo.getInherentJson(), Map.class);
  }

  /**
   * enum値取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @param field
   * @return
   */
  @SuppressWarnings({"unchecked"})
  @Cacheable(key = "{ #tenantId:#field }", value = "ServiceInstancesResources::getEnumValues")
  public List<String> getEnumValues(Integer tenantId, String field) {
    val vo = serviceInstancesMapper.selectServiceInstances(tenantId, "enum_" + field);
    Gson gson = new Gson();
    List<String> result = null;
    if (vo != null && vo.getInherentJson() != null) {
      val itemMap = gson.fromJson(vo.getInherentJson(), Map.class);
      if (itemMap.get("items") != null) {
        val items = (List<Map<String, Object>>) itemMap.get("items");
        result = items.stream().map((item) -> {
          var value = item.get("value");
          if (value instanceof Double) {
            value = ((Double) value).longValue();
          }
          return String.valueOf(value);
        }).collect(Collectors.toList());
      }
    }
    return result;
  }

  /**
   * title:value map取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2022/09/26
   * @updatedAt : 2022/09/26
   * @param tenantId
   * @param field
   * @return
   */
  @SuppressWarnings({"unchecked"})
  @Cacheable(key = "{ #tenantId:#field }", value = "ServiceInstancesResources::getEnumTitleMap")
  public Map<String, Long> getEnumTitleMap(Integer tenantId, String field) {
    val vo = serviceInstancesMapper.selectServiceInstances(tenantId, "enum_" + field);
    Gson gson = new Gson();
    Map<String, Long> result = null;
    try {
      if (vo != null && vo.getInherentJson() != null) {
        val itemMap = gson.fromJson(vo.getInherentJson(), Map.class);
        if (itemMap.get("items") != null) {
          val items = (List<Map<String, Object>>) itemMap.get("items");
          result = items.stream()
              .collect(Collectors.toMap((param) -> (String) param.get("title"), (param) -> {
                var value = param.get("value");
                if (value instanceof Double) {
                  return ((Double) value).longValue();
                }
                return Long.parseLong(String.valueOf(param.get("value")));
              }));
        }
      }
    } catch (Exception e) {
      return null;
    }
    return result;
  }

  /**
   * 更新日を比較して初期化する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/10/14
   * @param tenantId
   */
  public void resetCacheableToUpdatedAt(Integer tenantId) {
    Date result = serviceInstancesMapper.selectServiceInstancesMaxUpdatedAt(tenantId);
    if (result != null && result.getTime() > getUpdatedAt(tenantId)) {
      removeCacheable(tenantId);
    }
  }

  /**
   * 更新可能か判断する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/10/14
   * @updatedAt : 2022/10/14
   * @param tenantId
   * @param updatedAtTime
   * @return
   */
  public boolean isReset(Integer tenantId, long updatedAtTime) {
    if (updatedAtTime < getUpdatedAt(tenantId)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 更新日キャッシュ
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @return
   */
  @Cacheable(key = "{ #tenantId }", value = "ServiceInstancesResources::getUpdatedAt")
  public Long getUpdatedAt(Integer tenantId) {
    val result = serviceInstancesMapper.selectServiceInstancesMaxUpdatedAt(tenantId);
    if (result == null) {
      return null;
    }
    return result.getTime();
  }

  /**
   * キャッシュ初期化
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   */
  @CacheEvict(key = "{ #tenantId }", value = "ServiceInstancesResources::*", allEntries = true)
  private void removeCacheable(Integer tenantId) {

  }
}
