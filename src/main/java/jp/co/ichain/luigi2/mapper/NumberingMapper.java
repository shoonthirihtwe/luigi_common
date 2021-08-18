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
public interface NumberingMapper {

  /**
   * Lockをかける
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param map
   * @param tenantId
   * @param code
   */
  void pessimisticLockKey(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId, @Param("code") String code);

  /**
   * 次の番号を取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param map
   * @param tenantId
   * @return
   */
  String selectIncrementNumber(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId);

  /**
   * 新しいキー登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param map
   * @param tenantId
   * @param code
   */
  void insertNo(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId, @Param("code") String code,
      @Param("updatedBy") Object updatedBy);

  /**
   * 採番キーを更新
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param map
   * @param tenantId
   * @param code
   */
  void updateNo(@Param("tableInfo") Map<String, Object> map,
      @Param("tenantId") Integer tenantId, @Param("code") String code,
      @Param("updatedBy") Object updatedBy);
}
