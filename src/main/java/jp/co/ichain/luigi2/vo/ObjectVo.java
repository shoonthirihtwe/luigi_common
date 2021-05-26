package jp.co.ichain.luigi2.vo;

import java.lang.reflect.Field;

/**
 * 基本VO
 *
 * @author : [AOT] s.park
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-03-16
 */
public abstract class ObjectVo {
  /**
   * テナントIDセット
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-03-16
   * @updatedAt : 2021-03-16
   * @param tenantId
   */
  public void setTenantId(int tenantId) {
    try {
      Field field = this.getClass().getDeclaredField("tenantId");
      field.setAccessible(true);
      field.set(this, tenantId);
    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * 代理店IDセット
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-04-26
   * @updatedAt : 2021-04-26
   * @param agencyId
   */
  public void setAgencyId(int agencyId) {
    try {
      Field field = this.getClass().getDeclaredField("agencyId");
      field.setAccessible(true);
      field.set(this, agencyId);
    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * 更新者セット
   *
   * @author : [AOT] s.paku
   * @createdAt : 2021-03-16
   * @updatedAt : 2021-03-16
   * @param updatedUser
   */
  public void setUpdatedUser(int updatedUser) {
    try {
      Field field = this.getClass().getDeclaredField("updatedUser");
      field.setAccessible(true);
      field.set(this, updatedUser);
    } catch (Exception e) {
      // ignore
    }
  }
}
