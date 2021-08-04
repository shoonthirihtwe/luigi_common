package jp.co.ichain.luigi2.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.CodeMasterVo;
import jp.co.ichain.luigi2.vo.SalesProductsVo;
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

  /**
   * Lockをかける
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-19
   * @updatedAt : 2021-07-19
   * @param map
   * @param tenantId
   */
  void pessimisticLockKey(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId);

  String selectIncrementNumber(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId);

  /**
   * キーに値するデータが存在するかチェック
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param map
   * @param tenantId
   * @return
   */
  Boolean selectIsExistKey(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId, @Param("data") Object data);

  List<TenantsVo> selectTenants(@Param("updatedAt") Date updatedAt);

  List<ServiceInstancesVo> selectServiceInstances(@Param("updatedAt") Date updatedAt);

  List<CodeMasterVo> selectCodeMaster(@Param("updatedAt") Date updatedAt);

  Date selectLastUpdatedAt(@Param("table") String table);

  // バッチ日付を取得
  List<TenantsVo> getBatchDate(@Param("tenantIds") List<Integer> tenantIds);

  // 決済方法取得
  String selectFactoringCompanyCode(@Param("contractNo") String contractNo);

  // 営業日取得
  Date selectOpenDate(@Param("tenantId") Integer tenantId, @Param("date") Date date,
      @Param("count") Integer count);

  /**
   * 販売プラン取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-19
   * @updatedAt : 2021-07-19
   * @param paramMap
   * @return
   */
  SalesProductsVo selectSalesProducts(Map<String, Object> paramMap);

  /**
   * バッチ日付更新
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @param paramMap
   * @return
   */
  int updateBatchDate(Map<String, Object> paramMap);

  /**
   * 権限関数名取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @param paramMap
   * @return
   */
  List<String> selectFunctionId();
}
