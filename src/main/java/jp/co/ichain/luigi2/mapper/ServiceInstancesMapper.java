package jp.co.ichain.luigi2.mapper;

import java.util.Date;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;

/**
 * ServiceObjects Mapper
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2022-07-12
 * @updatedAt : 2022-07-12
 */
@Repository
@Luigi2Mapper
public interface ServiceInstancesMapper {

  /**
   * サービスインスタンス取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/22
   * @updatedAt : 2022/09/22
   * @param tenantId
   * @param sourceKey
   * @return
   */
  ServiceInstancesVo selectServiceInstances(@Param("tenantId") Integer tenantId,
      @Param("sourceKey") String sourceKey);



  /**
   * 更新日取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/23
   * @updatedAt : 2022/09/23
   * @param tenantId
   * @return
   */
  Date selectServiceInstancesMaxUpdatedAt(@Param("tenantId") Integer tenantId);
}
