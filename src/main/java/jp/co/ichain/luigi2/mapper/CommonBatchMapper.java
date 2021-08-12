package jp.co.ichain.luigi2.mapper;

import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.BillingDetailVo;
import jp.co.ichain.luigi2.vo.BillingHeaderVo;
import jp.co.ichain.luigi2.vo.DepositDetailsVo;
import jp.co.ichain.luigi2.vo.DepositHeadersVo;
import jp.co.ichain.luigi2.vo.PremiumHeadersVo;

/**
 * BillingBatchMapper
 * 
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-06-18
 * @updatedAt : 2021-06-18
 */
@Repository
@Luigi2Mapper
public interface CommonBatchMapper {

  Map<String, String> getMaxBillingHeaderNo(Map<String, Object> param);

  int insertBillingHeader(BillingHeaderVo billingHeaderVo);

  int insertBillingDetails(BillingDetailVo billingDetailVo);

  // 保険料premium_headersを作成
  void insertPremiumHeader(Map<String, Object> param);

  // max(保険料.保険料連番)
  PremiumHeadersVo getMaxPremiumSequenceNo(Map<String, Object> param);

  // 保険料入金詳細データを作成する。
  int insertDepositDetails(DepositDetailsVo depositDetailsVo);

  // 保険料入金ヘッダを作成する。
  int insertDepositHeaders(DepositHeadersVo depositHeadersVo);

  // 入金ヘッダの入力日単位で1からの連番
  int selectBatchNo(Date batchDate);

}
