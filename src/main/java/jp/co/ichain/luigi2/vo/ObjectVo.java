package jp.co.ichain.luigi2.vo;

import java.lang.reflect.Field;

/**
 * 基本VO
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-07
 * @updatedAt : 2021-06-07
 */
public abstract class ObjectVo {
  /**
   * テナントIDセット
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
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
   * 更新者セット
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-07
   * @updatedAt : 2021-06-07
   * @param updatedBy
   */
  public void setUpdatedUser(String updatedBy) {
    try {
      Field field = this.getClass().getDeclaredField("updatedBy");
      field.setAccessible(true);
      field.set(this, updatedBy);
    } catch (Exception e) {
      // ignore
    }
  }
}
