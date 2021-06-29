package jp.co.ichain.luigi2.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.CodeMasterVo;
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
  void pessimisticLockKey(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId);

  String selectIncrementNumber(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId);

  List<TenantsVo> selectTenants(@Param("updatedAt") Date updatedAt);

  List<ServiceInstancesVo> selectServiceInstances(@Param("updatedAt") Date updatedAt);

  List<CodeMasterVo> selectCodeMaster(@Param("updatedAt") Date updatedAt);

  Date selectLastUpdatedAt(@Param("table") String table);

  // バッチ日付を取得
  List<TenantsVo> getBatchDate(@Param("tenantIds") List<Integer> tenantIds);

  // 決済方法取得
  String selectFactoringCompanyCode(@Param("contractNo") String contractNo);
}
