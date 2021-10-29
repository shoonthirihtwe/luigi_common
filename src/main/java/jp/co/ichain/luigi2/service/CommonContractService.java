package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonContractMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.vo.ClaimContractSearchVo;
import jp.co.ichain.luigi2.vo.ClaimCustomerVo;
import jp.co.ichain.luigi2.vo.ContractsVo;
import jp.co.ichain.luigi2.vo.RiskHeadersVo;

/**
 * 共通契約関連サービス
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-27
 * @updatedAt : 2021-10-27
 */
@Service
public class CommonContractService {

  @Autowired
  CommonContractMapper mapper;

  /**
   * 保障内容照会
   *
   * @author : [VJP] タン, [AOT] g.kim
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-10-27
   * @return ResultListDto<ClaimContractSearchVo>
   * @throws SecurityException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws IOException
   */
  public ClaimContractSearchVo getRiskHeaders(Map<String, Object> param) throws SecurityException,
      IllegalArgumentException, IllegalAccessException, InstantiationException, IOException {
    // 指定なしの場合は当日
    if (param.get("baseDate") == null) {
      // 基準日
      Date baseDate = (Date) param.get("onlineDate");
      // パラメーターを設定
      param.put("baseDate", baseDate.getTime());
    }
    // 保障内容照会情報を取得
    ContractsVo contracts = mapper.selectContracts(param);

    if (contracts == null) {
      throw new WebException(Luigi2ErrorCode.C0001);
    }

    ClaimContractSearchVo claimContractSearchVo = new ClaimContractSearchVo();

    claimContractSearchVo.setTenantId(contracts.getTenantId()); // テナントID
    claimContractSearchVo.setContractNo(contracts.getContractNo()); // 証券番号
    claimContractSearchVo.setContractBranchNo(contracts.getContractBranchNo()); // 証券番号枝番

    param.put("contractBranchNo", contracts.getContractBranchNo());
    // 被保険者
    ClaimCustomerVo insured = mapper.selectInsured(param);
    claimContractSearchVo.setInsured(insured);

    // 死亡保険金受取人
    List<ClaimCustomerVo> beneficiaries = mapper.selectBeneficiaries(param);
    claimContractSearchVo.setBeneficiaries(beneficiaries);

    // 保障内容取得
    List<RiskHeadersVo> benefits = mapper.selectBenefit(param);
    claimContractSearchVo.setBenefits(benefits);

    return claimContractSearchVo;
  }

}
