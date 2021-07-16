package jp.co.ichain.luigi2.mapper;

import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.ContractLogVo;

/**
 * ContractLogMapper
 * 
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-07-16
 * @updatedAt : 2021-07-16
 */
@Repository
@Luigi2Mapper
public interface ContractLogMapper {
  // 契約ログの連番取得
  int selectSequenceNo(ContractLogVo contractLogVo);

  // 契約ログを作成
  void insertContractLog(ContractLogVo contractLogVo);
}
