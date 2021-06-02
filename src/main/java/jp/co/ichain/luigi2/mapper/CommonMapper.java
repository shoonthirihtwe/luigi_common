package jp.co.ichain.luigi2.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;

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
}
