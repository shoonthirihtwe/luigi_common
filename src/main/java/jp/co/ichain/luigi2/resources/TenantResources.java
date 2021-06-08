package jp.co.ichain.luigi2.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.util.CollectionUtils;
import jp.co.ichain.luigi2.vo.TenantsVo;
import lombok.val;

/**
 * TenantResources
 *
 * @author : [AOT] g.kim
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Singleton
@Service
public class TenantResources {

  private ConcurrentHashMap<Integer, TenantsVo> tenantMap = null;
  private Date updatedAt = null;

  @Autowired
  CommonMapper mapper;

  /**
   * メッセージリソースを初期化する
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-04-06
   * @updatedAt : 2021-04-06
   */
  @Lock(LockType.WRITE)
  @PostConstruct
  public void initialize() {
    this.tenantMap = new ConcurrentHashMap<Integer, TenantsVo>();

    val list = mapper.selectTenants(null);

    for (val tenantVo : CollectionUtils.safe(list)) {
      this.tenantMap.put(tenantVo.getId(), tenantVo);
    }

    this.updatedAt = mapper.selectTenantLastUpdatedAt();
  }

  /**
   * tenant情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param tenantVo
   * @return
   */
  @Lock(LockType.READ)
  public TenantsVo get(Integer id) {
    if (this.tenantMap == null) {
      this.initialize();
    }

    return this.tenantMap.get(id);
  }

  /**
   * 全Tenant取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   */
  @Lock(LockType.READ)
  public List<TenantsVo> getAll() {
    if (this.tenantMap == null) {
      this.initialize();
    }

    return new ArrayList<TenantsVo>(this.tenantMap.values());
  }

  /**
   * 最新更新日取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-04-06
   * @updatedAt : 2021-04-06
   * @return
   */
  public Date getUpdatedAt() {
    return updatedAt;
  }

  /**
   * 最新項目を取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-04-06
   * @updatedAt : 2021-04-06
   */
  public void refreshing() {

    if (this.tenantMap == null) {
      this.initialize();
    }

    val list = mapper.selectTenants(null);

    if (list.size() > 0) {
      this.updatedAt = list.get(0).getUpdatedAt();

      for (val tenantVo : CollectionUtils.safe(list)) {
        this.tenantMap.put(tenantVo.getId(), tenantVo);
      }
    }
  }

  /**
   * 最後更新日以降のメッセージ取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-04-06
   * @updatedAt : 2021-04-06
   * @param updatedAt
   * @return
   */
  public List<TenantsVo> getAllLastUpdatedDateAfter(Date updatedAt) {
    if (this.tenantMap == null) {
      this.initialize();
    }

    if (updatedAt == null || this.updatedAt == null
        || updatedAt.getTime() == this.updatedAt.getTime()) {
      return new ArrayList<TenantsVo>(this.tenantMap.values());
    } else if (updatedAt.getTime() < this.updatedAt.getTime()) {
      val srchVo = new TenantsVo();
      srchVo.setUpdatedAt(updatedAt);
      return mapper.selectTenants(srchVo);
    } else {
      throw new WebException(Luigi2Code.S0000);
    }
  }
}
