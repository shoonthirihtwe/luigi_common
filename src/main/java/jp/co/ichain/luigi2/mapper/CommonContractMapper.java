package jp.co.ichain.luigi2.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.ClaimCustomerVo;
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
}
