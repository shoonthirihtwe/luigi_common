package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonContractMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
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
   * @param param= {tenantId, customerIdList, onlineDate, updatedBy}
   * @param role ("PH", "IN")
   * @return SumUpCheckResultVo
   * @throws JSONException
   */
  public List<SumUpCheckResultVo> checkSumUp(Map<String, Object> param, String role, String table)
      throws JSONException {

    if (role.equals(Role.PH.toString()) || role.equals(Role.IN.toString())) {
      param.put("targetType", role);
    } else {
      return null;
    }

    val resultList = new ArrayList<SumUpCheckResultVo>();
    val sumUpBenefits = mapper.selectSumUpBenefits(param);


    for (val benefit : sumUpBenefits) {
      param.put("benefitGroupType", benefit.get("benefitGroupType"));
      val sumUpMaps = mapper.selectSumUpCheckMaps(param);
      if (sumUpMaps != null) {
        val result = new SumUpCheckResultVo();
        result.setBenefitGroupTypeCode(sumUpMaps.getBenefitGroupType());
        result.setBenefitGroupTypeName(sumUpMaps.getBenefitGroupTypeName());
        result.setResult(sumUpMaps.getSumUpAmount() >= (benefit.get("sumUpValue") == null ? 0
            : Integer.parseInt(String.valueOf(benefit.get("sumUpValue")))));
        result.setTargetType(role);
        resultList.add(result);
      }
    }

    insertSumUpCheckResult(param, resultList, table, role);
    return resultList;
  }

  /**
   * 通算チェック結果更新
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-09-27
   * @updatedAt : 2022-09-27
   */
  private void insertSumUpCheckResult(Map<String, Object> param,
      List<SumUpCheckResultVo> resultList, String table, String role) throws JSONException {

    boolean isPolicy = false;
    if (role.equals(Role.PH.toString())) {
      isPolicy = true;
    }
    JSONArray policyHolderCheckList = new JSONArray();
    JSONArray insuredCheckList = new JSONArray();
    for (val result : resultList) {

      JSONObject check = new JSONObject();
      check.put("benefit_group_type_code", result.getBenefitGroupTypeCode());
      check.put("benefit_group_type_name", result.getBenefitGroupTypeName());
      check.put("result", result.getResult());
      if (isPolicy) {
        policyHolderCheckList.put(check);
      } else {
        insuredCheckList.put(check);
      }
    }
    if (isPolicy) {
      param.put("policyHolderJson", policyHolderCheckList.toString());
    } else {
      param.put("insuredJson", insuredCheckList.toString());
    }
    mapper.updateSumupCheckResult(param, table);
  }
}
