package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.util.CollectionUtils;
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
public class TenantResources {

  private ConcurrentHashMap<Integer, TenantsVo> tenantMap = null;
  private ConcurrentHashMap<String, TenantsVo> tenantToDomainMap = null;
  private Date updatedAt = null;

  @Autowired
  CommonMapper mapper;

  /**
   * メッセージリソースを初期化する
   *
   * @author : [AOT] g.kim
   * @throws GrpcException
   * @throws InvalidProtocolBufferException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   */
  @Lock(LockType.WRITE)
  @PostConstruct
  public void initialize()
      throws InstantiationException, IllegalAccessException, SecurityException {
    this.tenantMap = new ConcurrentHashMap<Integer, TenantsVo>();
    this.tenantToDomainMap = new ConcurrentHashMap<String, TenantsVo>();

    this.initializeTenants(mapper.selectTenants(null));
  }

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
  @Lock(LockType.READ)
  public TenantsVo get(Integer id)
      throws InstantiationException, IllegalAccessException, SecurityException {
    if (this.tenantMap == null) {
      this.initialize();
    }

    return this.tenantMap.get(id);
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
  @Lock(LockType.READ)
  public TenantsVo get(String domain)
      throws InstantiationException, IllegalAccessException, SecurityException {
    if (this.tenantToDomainMap == null) {
      this.initialize();
    }

    return this.tenantToDomainMap.get(domain);
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
  @Lock(LockType.READ)
  public List<TenantsVo> getAll()
      throws InstantiationException, IllegalAccessException, SecurityException {
    if (this.tenantMap == null) {
      this.initialize();
    }

    return new ArrayList<TenantsVo>(this.tenantMap.values());
  }

  /**
   * 最新更新日取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   */
  public Date getUpdatedAt() {
    return updatedAt;
  }

  /**
   * 最新項目を取得
   *
   * @author : [AOT] g.kim
   * @throws GrpcException
   * @throws InvalidProtocolBufferException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   */
  public void refreshing()
      throws InstantiationException, IllegalAccessException, SecurityException {

    if (this.tenantMap == null) {
      this.initialize();
    }

    this.initializeTenants(mapper.selectTenants(this.updatedAt));
  }

  /**
   * 初期化する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-17
   * @updatedAt : 2021-06-17
   * @param list
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws SecurityException
   */
  private void initializeTenants(List<TenantsVo> list)
      throws InstantiationException, IllegalAccessException, SecurityException {
    if (this.tenantMap == null) {
      this.initialize();
    }

    if (CollectionUtils.isEmpty(list) == false) {
      this.updatedAt =
          list.stream().map(vo -> vo.getUpdatedAt() != null ? vo.getUpdatedAt() : vo.getCreatedAt())
              .max(Comparator.comparing(updatedAt -> updatedAt.getTime()))
              .orElseThrow(() -> new WebDataException(Luigi2Code.D0002));

      for (val tenantVo : CollectionUtils.safe(list)) {
        this.tenantMap.put(tenantVo.getId(), tenantVo);
        this.tenantToDomainMap.put(tenantVo.getSiteUrl(), tenantVo);
      }
    }
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
  public List<TenantsVo> getAllLastUpdatedDateAfter(Date updatedAt)
      throws InstantiationException, IllegalAccessException, SecurityException {
    if (this.tenantMap == null) {
      this.initialize();
    }

    if (updatedAt == null || this.updatedAt == null
        || updatedAt.getTime() == this.updatedAt.getTime()) {
      return new ArrayList<TenantsVo>(this.tenantMap.values());
    } else if (updatedAt.getTime() < this.updatedAt.getTime()) {
      return mapper.selectTenants(updatedAt);
    } else {
      throw new WebException(Luigi2Code.S0000);
    }
  }
}
