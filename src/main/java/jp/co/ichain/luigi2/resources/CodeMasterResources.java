package jp.co.ichain.luigi2.resources;

import java.util.Comparator;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
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
  CommonMapper commonMapper;

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

    val list = commonMapper.selectCodeMaster(null);
    Map<Integer, List<CodeMasterVo>> tenantGroupMap =
        list.stream().collect(Collectors.groupingBy(vo -> vo.getTenantId()));


    for (val tenantId : tenantGroupMap.keySet()) {
      val listByTenant = tenantGroupMap.get(tenantId);

      // last updatedAt
      Date maxValue = listByTenant.stream()
          .map(vo -> vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt())
          .max(Comparator.comparing(updatedAt -> updatedAt.getTime()))
          .orElseThrow(() -> new WebDataException(Luigi2ErrorCode.D0002));
      updatedAtMap.put(tenantId, maxValue);

      Map<String, List<CodeMasterVo>> tableGroupMap =
          listByTenant.stream().collect(Collectors.groupingBy(vo -> vo.getTbl()));

      this.map.put(tenantId, tableGroupMap);
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

  /**
   * 最新更新日取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param tenantId
   * @return
   */
  public Date getUpdatedAt(Integer tenantId) {
    return updatedAtMap.get(tenantId);
  }

  /**
   * 最後更新日以降取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param updatedAt
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Map<String, List<CodeMasterVo>> getAllLastUpdatedDateAfter(Integer tenantId,
      Date updatedAt) throws JsonMappingException, JsonProcessingException {
    if (this.map == null) {
      this.initialize();
    }

    Date lastUpdatedAt = updatedAtMap.get(tenantId);
    val mapByTenant = this.get(tenantId);

    if (updatedAt == null || lastUpdatedAt == null
        || updatedAt.getTime() == lastUpdatedAt.getTime()) {
      return this.get(tenantId);
    } else if (updatedAt.getTime() < lastUpdatedAt.getTime()) {
      val result = new HashMap<String, List<CodeMasterVo>>();
      for (val key : mapByTenant.keySet()) {
        val list = mapByTenant.get(key);
        result.put(key,
            list.stream().filter(vo -> updatedAt.getTime() < vo.getUpdatedAt().getTime())
                .collect(Collectors.toList()));
      }
      return result;
    } else {
      throw new WebException(Luigi2ErrorCode.S0000);
    }
  }

}
