package jp.co.ichain.luigi2.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.google.protobuf.InvalidProtocolBufferException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.vo.TenantsVo;
import lombok.val;

/**
 * Tenantリソースを取得
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Singleton
@Service
@DependsOn(value = {"dataSourceInitializer"})
public class TenantResources {

  @Autowired
  CommonMapper mapper;

  /**
   * tenant情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param tenantVo
   * @return
   * @throws GrpcException
   * @throws InvalidProtocolBufferException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public TenantsVo get(Integer id) {
    val param = new HashMap<String, Object>();
    param.put("id", id);

    val result = mapper.selectTenants(param);
    return result.size() == 0 ? null : result.get(0);
  }

  /**
   * tenant情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-17
   * @updatedAt : 2021-06-17
   * @param domain
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws SecurityException
   * @throws InvalidProtocolBufferException
   * @throws GrpcException
   */
  public TenantsVo get(String domain) {

    val param = new HashMap<String, Object>();
    param.put("domain", domain);

    val result = mapper.selectTenants(param);
    return result.size() == 0 ? null : result.get(0);
  }

  /**
   * 全Tenant取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   * @throws GrpcException
   * @throws InvalidProtocolBufferException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public List<TenantsVo> getAll() {
    return mapper.selectTenants(null);
  }

  /**
   * 最後更新日以降のメッセージ取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param updatedAt
   * @return
   * @throws GrpcException
   * @throws InvalidProtocolBufferException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public List<TenantsVo> getAllLastUpdatedDateAfter(Date updatedAt) {
    val param = new HashMap<String, Object>();
    param.put("updatedAt", updatedAt);
    return mapper.selectTenants(param);
  }
}
