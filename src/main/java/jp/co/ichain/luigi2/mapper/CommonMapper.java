package jp.co.ichain.luigi2.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.CodeMasterVo;
import jp.co.ichain.luigi2.vo.FactoringCompaniesVo;
import jp.co.ichain.luigi2.vo.SalesProductsVo;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import jp.co.ichain.luigi2.vo.TenantsVo;
import jp.co.ichain.luigi2.vo.UpdateStatusVo;

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

  /**
   * テナント取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param updatedAt
   * @return
   */
  List<TenantsVo> selectTenants(Map<String, Object> param);

  /**
   * ServiceInstancesBaseData取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-05
   * @updatedAt : 2022-09-05
   * @return
   */
  List<ServiceInstancesVo> selectServiceInstancesBaseData(@Param("tenantId") Integer tenantId,
      @Param("businessGroupType") String businessGroupType);

  /**
   * ServiceInstancesBaseData取得 (sourceKey)
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/07
   * @updatedAt : 2022/09/07
   * @param tenantId
   * @param sourceKey
   * @return
   */
  List<ServiceInstancesVo> selectServiceInstancesBaseDataBySourceKey(
      @Param("tenantId") Integer tenantId, @Param("sourceKey") String sourceKey);

  /**
   * コードマスター取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param updatedAt
   * @return
   */
  List<CodeMasterVo> selectCodeMaster(@Param("updatedAt") Date updatedAt);

  /**
   * 最終更新日取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param table
   * @return
   */
  Date selectLastUpdatedAt(@Param("table") String table);

  /**
   * 決済情報取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param tenantId
   * @param contractNo
   * @return
   */
  FactoringCompaniesVo selectFactoringCompanyCode(@Param("tenantId") Integer tenantId,
      @Param("contractNo") String contractNo, @Param("nowDate") Date nowDate);

  /**
   * 営業日取得
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param tenantId
   * @param date
   * @param count
   * @return
   */
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
   * オンライン日付更新
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-29
   * @updatedAt : 2021-07-29
   * @param paramMap
   * @return
   */
  int updateOnlineDate(Map<String, Object> paramMap);

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

  /**
   * リソース更新用の更新日取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-13
   * @updatedAt : 2021-08-13
   * @return
   */
  List<Map<String, Object>> selectResourcesLastUpdatedAt();

  /**
   * 固有情報取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-11
   * @updatedAt : 2021-10-11
   * @param param
   * @return
   */
  String selectInherentData(Map<String, Object> param);

  /**
   * ステータス変更
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-11
   * @updatedAt : 2021-10-11
   * @param paramMap
   * @return
   */
  int updateTableStatus(UpdateStatusVo param);

  /**
   * テナント向け送信用メールアドレス取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-11
   * @updatedAt : 2021-10-11
   * @param paramMap
   * @return
   */
  String selectSenderEmailsByContractNo(@Param("recieverInfo") String recieverInfo,
      @Param("mailType") String mailType, @Param("param") Map<String, Object> param);

}
