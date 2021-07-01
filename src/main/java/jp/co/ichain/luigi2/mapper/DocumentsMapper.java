package jp.co.ichain.luigi2.mapper;

import java.util.Map;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;

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
  void insertDocuments(Map<String, Object> dataMap);
}
