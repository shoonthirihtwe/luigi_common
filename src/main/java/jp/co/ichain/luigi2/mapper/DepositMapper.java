package jp.co.ichain.luigi2.mapper;

import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.DepositDetailsVo;
import jp.co.ichain.luigi2.vo.DepositHeadersVo;

/**
 * DepositMapper
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-08-11
 * @updatedAt : 2021-08-11
 */
@Repository
@Luigi2Mapper
public interface DepositMapper {
  // 保険料入金詳細データを作成する。
  int insertDepositDetails(DepositDetailsVo depositDetailsVo);

  // 保険料入金ヘッダを作成する。
  int insertDepositHeaders(DepositHeadersVo depositHeadersVo);
}
