package jp.co.ichain.luigi2.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.DocumentsVo;

/**
 * Documents Mapper
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-01
 * @updatedAt : 2021-07-01
 */
@Repository
@Luigi2Mapper
public interface DocumentsMapper {

  /**
   * 文書追加
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-06
   * @updatedAt : 2021-07-06
   * @param dataMap
   */
  void insertDocuments(Map<String, Object> dataMap);

  /**
   * 文書取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-06
   * @updatedAt : 2021-07-06
   * @param dataMap
   * @return
   */
  List<DocumentsVo> selectDocuments(Map<String, Object> dataMap);

  List<DocumentsVo> selectAllDocumentsUrl(@Param("table") String table);
}
