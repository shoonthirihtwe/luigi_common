package jp.co.ichain.luigi2.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.mapper.ContractLogMapper;
import jp.co.ichain.luigi2.vo.ContractLogVo;
import jp.co.ichain.luigi2.vo.MaintenanceRequestsVo;
import lombok.val;

/**
 * 証券ログサービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-11
 * @updatedAt : 2021-08-11
 */
@Service
public class ContractLogService {

  @Autowired
  ContractLogMapper contractLogMapper;

  private static final Map<String, String> DESCRIPTION_MAP;

  static {
    DESCRIPTION_MAP = new HashMap<String, String>();

    // 保全 020
    DESCRIPTION_MAP.put("02001", "保全（保全）請求　受付完了");
    DESCRIPTION_MAP.put("02002", "保全（保全）請求　１次査定完了");
    DESCRIPTION_MAP.put("02003", "保全（保全）請求　２次査定完了");
    DESCRIPTION_MAP.put("02004", "保全（保全）請求　１次査定者へ差戻し");
    DESCRIPTION_MAP.put("02005", "保全（保全）請求　不備");
    DESCRIPTION_MAP.put("02006", "保全（保全）請求　取下");

    // 請収 030
    DESCRIPTION_MAP.put("03001", "保険料請求（カード日次）");
    DESCRIPTION_MAP.put("03002", "保険料請求（カード月次）");
    DESCRIPTION_MAP.put("03003", "保険料請求（口振）");
    DESCRIPTION_MAP.put("03011", "保険料収納（カード日次）");
    DESCRIPTION_MAP.put("03012", "保険料収納（カード月次）");
    DESCRIPTION_MAP.put("03013", "保険料収納（口振）");
    DESCRIPTION_MAP.put("03021", "保険料未納（カード日次）");
    DESCRIPTION_MAP.put("03022", "保険料未納（カード月次）");
    DESCRIPTION_MAP.put("03023", "保険料未納（口振）");
    DESCRIPTION_MAP.put("03090", "失効");

    // 保険金 040
    DESCRIPTION_MAP.put("04001", "保険金（給付金）請求受付完了");
    DESCRIPTION_MAP.put("04011", "保険金（給付金）請求　１次査定完了");
    DESCRIPTION_MAP.put("04021", "保険金（給付金）請求　２次査定完了");
    DESCRIPTION_MAP.put("04022", "保険金（給付金）請求　１次査定者へ差戻し");
    DESCRIPTION_MAP.put("04023", "保険金（給付金）請求　契約者へ差戻し");

    // 更新 050
    DESCRIPTION_MAP.put("05001", "反社チェック済");
    DESCRIPTION_MAP.put("05002", "更新通知済");
    DESCRIPTION_MAP.put("05003", "満了処理済");
    DESCRIPTION_MAP.put("05004", "更新済");
    DESCRIPTION_MAP.put("05024", "契約満了");
    DESCRIPTION_MAP.put("05025", "契約更新");
    DESCRIPTION_MAP.put("05028", "契約満了");
    DESCRIPTION_MAP.put("05029", "契約満了受付");

    // 経理 120
    DESCRIPTION_MAP.put("12001", "保険金（給付金）支払の振込用データ作成");
  }

  /**
   * 証券ログ登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-11
   * @updatedAt : 2021-08-11
   * @param contractLogVo
   */
  public void registerContractLog(ContractLogVo contractLogVo) {
    if (contractLogVo.getDescription() == null) {
      contractLogVo
          .setDescription(DESCRIPTION_MAP
              .get(contractLogVo.getReasonCode() + contractLogVo.getContactTransactionCode()));
    }
    contractLogMapper.insertContractLog(contractLogVo);
  }

  /**
   * 証券ログ登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-11
   * @updatedAt : 2021-08-11
   * @param tenantId
   * @param contractNo
   * @param contractBranchNo
   * @param reasonGroupCode
   * @param reasonCode
   * @param contactTransactionCode
   * @param programName
   * @param createdBy
   */
  public void registerContractLog(Integer tenantId, String contractNo, String contractBranchNo,
      String reasonGroupCode,
      String reasonCode, String contactTransactionCode, String programName, Object createdBy) {
    val contractLogVo = new ContractLogVo();
    contractLogVo.setTenantId(tenantId);
    contractLogVo.setContractNo(contractNo);
    contractLogVo.setContractBranchNo(contractBranchNo);
    contractLogVo.setReasonGroupCode(reasonGroupCode);
    contractLogVo.setReasonCode(reasonCode);
    contractLogVo.setContactTransactionCode(contactTransactionCode);
    contractLogVo.setProgramName(programName);
    contractLogVo.setCreatedBy(createdBy);
    this.registerContractLog(contractLogVo);
  }

  /**
   * 証券ログ登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-11
   * @updatedAt : 2021-08-11
   * @param tenantId
   * @param contractNo
   * @param contractBranchNo
   * @param reasonGroupCode
   * @param reasonCode
   * @param contactTransactionCode
   * @param programName
   * @param createdBy
   */
  public void registerContractLog(Integer tenantId, String contractNo, String reasonGroupCode,
      String reasonCode, String contactTransactionCode, String programName, Object createdBy) {
    val contractLogVo = new ContractLogVo();
    contractLogVo.setTenantId(tenantId);
    contractLogVo.setContractNo(contractNo);
    contractLogVo.setReasonGroupCode(reasonGroupCode);
    contractLogVo.setReasonCode(reasonCode);
    contractLogVo.setContactTransactionCode(contactTransactionCode);
    contractLogVo.setProgramName(programName);
    contractLogVo.setCreatedBy(createdBy);
    this.registerContractLog(contractLogVo);
  }

  /**
   * 証券ログ登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-11
   * @updatedAt : 2021-08-11
   * @param tenantId
   * @param requestNo
   * @param reasonGroupCode
   * @param reasonCode
   * @param contactTransactionCode
   * @param programName
   * @param createdBy
   */
  public void registerContractLogForMaintenanceRequests(Integer tenantId, String requestNo,
      String reasonGroupCode,
      String reasonCode, String contactTransactionCode, String programName, Object createdBy) {
    val contractLogVo = new ContractLogVo();
    contractLogVo.setTenantId(tenantId);
    contractLogVo.setRequestNo(requestNo);
    contractLogVo.setReasonGroupCode(reasonGroupCode);
    contractLogVo.setReasonCode(reasonCode);
    contractLogVo.setContactTransactionCode(contactTransactionCode);
    contractLogVo.setProgramName(programName);
    contractLogVo.setCreatedBy(createdBy);
    this.registerContractLog(contractLogVo);
  }

  /**
   * 証券ログ登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-11
   * @updatedAt : 2021-08-11
   * @param maintenanceVo
   * @param tenantId
   * @param reasonGroupCode
   * @param reasonCode
   * @param contactTransactionCode
   * @param programName
   * @param object
   */
  public void registerContractLogForMaintenanceRequests(MaintenanceRequestsVo maintenanceVo,
      Integer tenantId,
      String reasonGroupCode, String reasonCode, String contactTransactionCode, String programName,
      Object object) {
    val contractLogVo = new ContractLogVo();
    contractLogVo.setTenantId(tenantId);
    contractLogVo.setRequestNo(maintenanceVo.getRequestNo());
    contractLogVo.setReasonGroupCode(reasonGroupCode);
    contractLogVo.setReasonCode(reasonCode);
    contractLogVo.setContactTransactionCode(contactTransactionCode);
    contractLogVo.setProgramName(programName);
    this.registerContractLog(contractLogVo);
  }

}
