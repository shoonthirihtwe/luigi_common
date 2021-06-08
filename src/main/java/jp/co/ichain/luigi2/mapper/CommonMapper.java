package jp.co.ichain.luigi2.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import jp.co.ichain.luigi2.vo.TenantsVo;

/**
 * Common Mapper
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-02
 * @updatedAt : 2021-06-02
 */
@Repository
@Luigi2Mapper
public interface CommonMapper {
  void pessimisticLockKey(@Param("tableInfo") Map<String, String> map,
      @Param("tenantId") Integer tenantId);

  String selectIncrementNumber(@Param("tableInfo") Map<String, String> map,
      @Param("tenantId") Integer tenantId);

  List<TenantsVo> selectTenants(TenantsVo tenantVo);

  Date selectTenantLastUpdatedAt();

  List<ServiceInstancesVo> selectServiceInstances(@Param("updatedAt") Date updatedAt);

  Date selectLastUpdatedAt(@Param("table") String table);
}
