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

  /**
   * 請求billing_headerのBillingHeaderNoの最大値取得
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  int selectMaxBillingHeaderNo(Map<String, Object> param);

  /**
   * 請求billing_headerのBillingHeaderNoの値取得
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-09-01
   * @updatedAt : 2021-09-01
   * @param param
   * @return
   */
  Integer selectBillingHeaderNo(Map<String, Object> param);

  /**
   * 請求billing_headerを作成
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  int insertBillingHeader(BillingHeaderVo billingHeaderVo);

  /**
   * 請求詳細billing_header_detailを作成
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  int insertBillingDetails(BillingDetailVo billingDetailVo);

  /**
   * 保険料premium_headersを作成
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  void insertPremiumHeader(Map<String, Object> param);

  /**
   * max(保険料.保険料連番)
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  PremiumHeadersVo getMaxPremiumSequenceNo(Map<String, Object> param);

  /**
   * 保険料入金詳細データを作成する。
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  int insertDepositDetails(DepositDetailsVo depositDetailsVo);

  /**
   * 保険料入金ヘッダを作成する。
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  int insertDepositHeaders(DepositHeadersVo depositHeadersVo);

  /**
   * 入金ヘッダの入力日単位で1からの連番
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-06-18
   * @updatedAt : 2021-06-18
   * @param param
   * @return
   */
  int selectBatchNo(Date batchDate);


  /**
   * 請求テーブルの請求ヘッダー状態コード変更
   * 
   * @author : [VJP] n.h.hoang
   * @createdAt : 2021-09-13
   * @updatedAt : 2021-09-13
   * @param billingHeader
   */
  void updateBillingHeader(BillingHeaderVo billingHeaderVo);

  /**
   * 請求テーブルに取引用IDセット
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-19
   * @updatedAt : 2022-04-19
   * @param depositDetailsVo
   */
  void updateBillingDetail(DepositDetailsVo depositDetailsVo);
}
