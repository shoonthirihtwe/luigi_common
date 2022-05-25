package jp.co.ichain.luigi2.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;

@Repository
@Luigi2Mapper
public interface RoutineMapper {

  /**
   * 固有ロジック取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-05-24
   * @updatedAt : 2022-05-24
   * @param param
   * @return
   */
  List<Map<String, Object>> selectServiceInstances(Map<String, Object> param);
}
