package jp.co.ichain.luigi2.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.ServiceObjectsVo;

/**
 * ServiceObjects Mapper
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2022-07-12
 * @updatedAt : 2022-07-12
 */
@Repository
@Luigi2Mapper
public interface ServiceObjectsMapper {

  /**
   * serviceObjects Max Version取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-07-12
   * @updatedAt : 2022-07-12
   * @param param
   * @return
   */
  int selectMaxVersion(Map<String, Object> param);


  /**
   * サービスObject取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-06-22
   * @updatedAt : 2022-06-22
   * @param param
   * @return
   */
  List<ServiceObjectsVo> selectServiceObjects(Map<String, Object> param);

  /**
   * ServiceObjects挿入
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-07-12
   * @updatedAt : 2022-07-12
   * @param param
   */
  void insert(Map<String, Object> param);


  /**
   * ServiceObjects更新
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-07-12
   * @updatedAt : 2022-07-12
   * @param param
   */
  int update(Map<String, Object> param);

  /**
   * ServiceObjects削除
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-07-12
   * @updatedAt : 2022-07-12
   * @param param
   */
  void delete(Map<String, Object> param);

  /**
   * ServiceObjects削除
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-07-13
   * @updatedAt : 2022-07-13
   * @param param
   */
  void deleteAllSequenceNo(Map<String, Object> param);
}
