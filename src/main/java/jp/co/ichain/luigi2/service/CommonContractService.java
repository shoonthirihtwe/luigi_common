package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonContractMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeMaintenanceRequestsCustomer.Role;
import jp.co.ichain.luigi2.vo.ClaimContractSearchVo;
import jp.co.ichain.luigi2.vo.ClaimCustomerVo;
import jp.co.ichain.luigi2.vo.ClaimHeadersVo;
import jp.co.ichain.luigi2.vo.ContractsVo;
import jp.co.ichain.luigi2.vo.RiskHeadersVo;
import jp.co.ichain.luigi2.vo.SumUpCheckResultVo;
import lombok.val;

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

    // 保障内容照会情報を取得
    ContractsVo contracts = mapper.selectContracts(param);

    if (contracts == null) {
      throw new WebException(Luigi2ErrorCode.C0001);
    }

    // 指定なしの場合は当日
    if (param.get("baseDate") == null) {
      // 基準日
      Date baseDate = (Date) param.get("onlineDate");
      // パラメーターを設定
      param.put("baseDate", baseDate.getTime());
    }

    ClaimContractSearchVo claimContractSearchVo = new ClaimContractSearchVo();

    claimContractSearchVo.setTenantId(contracts.getTenantId()); // テナントID
    claimContractSearchVo.setContractNo(contracts.getContractNo()); // 証券番号
    claimContractSearchVo.setContractBranchNo(contracts.getContractBranchNo()); // 証券番号枝番

    param.put("contractBranchNo", contracts.getContractBranchNo());
    // 被保険者
    ClaimCustomerVo insured = mapper.selectInsured(param);
    claimContractSearchVo.setInsured(insured);

    // 請求者情報
    ClaimHeadersVo claimHeader = mapper.selectClaimHeader(param);
    claimContractSearchVo.setClaimHeader(claimHeader);

    // 死亡保険金受取人
    List<ClaimCustomerVo> beneficiaries = mapper.selectBeneficiaries(param);
    claimContractSearchVo.setBeneficiaries(beneficiaries);

    // 保障内容取得
    List<RiskHeadersVo> benefits = mapper.selectBenefit(param);
    claimContractSearchVo.setBenefits(benefits);

    return claimContractSearchVo;
  }

  /**
   * 通算チェック
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-27
   * @updatedAt : 2022-09-27
   * @param param= {tenantId, customerId, onlineDate, updatedBy}
   * @param role ("PH", "IN")
   * @return SumUpCheckResultVo
   * @throws JSONException
   */
  public SumUpCheckResultVo checkSumUp(Map<String, Object> param, String role, TableInfo table)
      throws JSONException {

    val result = new SumUpCheckResultVo();
    if (role.equals(Role.PH.toString()) || role.equals(Role.IN.toString())) {
      param.put("targetType", role);
      result.setTargetType(role);
    } else {
      return null;
    }

    val benefitSumUp = mapper.selectSumUpBenefits(param);

    int sum = 0;
    int maxSum = 0;
    for (val benefit : benefitSumUp) {
      if (benefit.getBenefitGroupTypeBl() != null) {
        sum += benefit.getSumUpValue();
        param.put("benefitGroupTypeBl", benefit.getBenefitGroupTypeBl());
        param.put("benefitGroupType", benefit.getBenefitGroupTypeBl());
        sum += mapper.selectSumUpRiskHeaders(param);
        maxSum += mapper.selectSumUpAmount(param);
      }
    }
    result.setBenefitGroupTypeBlResult(sum <= maxSum);
    param.put("benefitGroupTypeBl", null);
    sum = 0;
    maxSum = 0;
    for (val benefit : benefitSumUp) {
      if (benefit.getBenefitGroupTypeBylaw() != null) {
        sum += benefit.getSumUpValue();
        param.put("benefitGroupTypeBylaw", benefit.getBenefitGroupTypeBylaw());
        param.put("benefitGroupType", benefit.getBenefitGroupTypeBylaw());
        sum += mapper.selectSumUpRiskHeaders(param);
        maxSum += mapper.selectSumUpAmount(param);
      }
    }
    result.setBenefitGroupTypeBylawResult(sum <= maxSum);
    insertSumUpCheckResult(param, result, table);
    return result;
  }

  /**
   * 通算チェック結果更新
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-27
   * @updatedAt : 2022-09-27
   */
  private void insertSumUpCheckResult(Map<String, Object> param, SumUpCheckResultVo result,
      TableInfo table) throws JSONException {

    JSONObject policyHolderBl = new JSONObject();
    policyHolderBl.put("target",
        result.getTargetType().equals(Role.PH.toString()) ? "契約者" : "被保険者");
    policyHolderBl.put("result", result.getBenefitGroupTypeBlResult());
    JSONObject businessLaw = new JSONObject();
    businessLaw.put("check_name", "通算チェック　業法");
    businessLaw.put("policy_holder", policyHolderBl);

    JSONObject policyHolderBylaw = new JSONObject();
    policyHolderBylaw.put("target",
        result.getTargetType().equals(Role.PH.toString()) ? "契約者" : "被保険者");
    policyHolderBylaw.put("result", result.getBenefitGroupTypeBlResult());
    JSONObject businessBylaw = new JSONObject();
    businessBylaw.put("check_name", "通算チェック　業法");
    businessBylaw.put("policy_holder", policyHolderBylaw);
    JSONObject sumUpCheck = new JSONObject();
    sumUpCheck.put("business_law", businessLaw);
    sumUpCheck.put("bylaw", businessBylaw);
    param.put("json", sumUpCheck);
    mapper.updateSumupCheckResult(param, table.name().toString());
  }
}
