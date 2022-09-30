package jp.co.ichain.luigi2.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.BenefitsVo;
import jp.co.ichain.luigi2.vo.ClaimCustomerVo;
import jp.co.ichain.luigi2.vo.ClaimHeadersVo;
import jp.co.ichain.luigi2.vo.ContractsVo;
import jp.co.ichain.luigi2.vo.RiskHeadersVo;

/**
 * ContractMapper
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-27
 * @updatedAt : 2021-10-27
 */
@Repository
@Luigi2Mapper
public interface CommonContractMapper {

  /**
   * 保障内容照会
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param param
   * @return
   */
  ContractsVo selectContracts(Map<String, Object> param);

  /**
   * 保障内容を取得
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param param
   * @return
   */
  List<RiskHeadersVo> selectBenefit(Map<String, Object> param);

  /**
   * 請求者情報
   * 
   * @author : [VJP] ホアン
   * @createdAt : 2021-10-30
   * @updatedAt : 2021-10−30
   * @param param
   * @return
   */
  ClaimHeadersVo selectClaimHeader(Map<String, Object> param);

  /**
   * 被保険者取得
   * 
   * @author : [VJP] ホアン
   * @createdAt : 2021-10-28
   * @updatedAt : 2021-10−28
   * @param param
   * @return
   */
  ClaimCustomerVo selectInsured(Map<String, Object> param);

  /**
   * 死亡保険金受取人取得
   * 
   * @author : [VJP] ホアン
   * @createdAt : 2021-10-28
   * @updatedAt : 2021-10−28
   * @param param
   * @return
   */
  List<ClaimCustomerVo> selectBeneficiaries(Map<String, Object> param);

  /**
   * 最新証券番号枝番取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2022-07-13
   * @updatedAt : 2022-07−13
   * @param param
   * @return
   */
  String selectMaxContractBranchNo(Map<String, Object> param);

  /**
   * Benefits合計値取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-28
   * @updatedAt : 2022-09-28
   * @param
   * @return
   */
  List<BenefitsVo> selectSumUpBenefits(Map<String, Object> param);

  /**
   * RiskHeaders合計値取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-28
   * @updatedAt : 2022-09-28
   * @param
   * @return
   */
  Integer selectSumUpRiskHeaders(Map<String, Object> param);

  /**
   * 上限額取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-28
   * @updatedAt : 2022-09-28
   * @param
   * @return
   */
  Integer selectSumUpAmount(Map<String, Object> param);

  /**
   * 通算チェック結果更新
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-28
   * @updatedAt : 2022-09-28
   * @param
   * @return
   */
  Integer updateSumupCheckResult(Map<String, Object> param, @Param("tableName") String tableName);
}
