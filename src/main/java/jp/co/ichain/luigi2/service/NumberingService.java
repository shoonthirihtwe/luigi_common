package jp.co.ichain.luigi2.service;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.mapper.NumberingMapper;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
import jp.co.ichain.luigi2.resources.TenantResources;
import lombok.val;

/**
 * 採番サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-18
 * @updatedAt : 2021-08-18
 */
@Service
public class NumberingService {

  @Autowired
  TenantResources tenantResources;

  @Autowired
  NumberingMapper numberingMapper;
  
  @Autowired
  CommonMapper mapper;

  private Date updatedAt;
  
  /**
   * 起動時採番
   * 
   * @author : [AOT] s.paku
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   */
  @Lock(LockType.WRITE)
  @PostConstruct
  public void initialize()
      throws InstantiationException, IllegalAccessException, SecurityException {
    for (val tenant : tenantResources.getAll()) {
      val tenantId = tenant.getId();
      if (numberingMapper.selectIncrementNumber(Luigi2TableInfo.getLockTable(TableInfo.Contracts),
          tenantId).equals("0")) {
        numberingMapper.insertNo(Luigi2TableInfo.getLockTable(TableInfo.Contracts), tenantId, null,
            "cachingAI");
      }
      if (numberingMapper.selectIncrementNumber(
          Luigi2TableInfo.getLockTable(TableInfo.MaintenanceRequests), tenantId).equals("0")) {
        numberingMapper.insertNo(Luigi2TableInfo.getLockTable(TableInfo.MaintenanceRequests),
            tenantId, null, "cachingAI");
      }
      if (numberingMapper.selectIncrementNumber(
          Luigi2TableInfo.getLockTable(TableInfo.ClaimTrxsId), tenantId).equals("0")) {
        numberingMapper.insertNo(Luigi2TableInfo.getLockTable(TableInfo.ClaimTrxsId),
            tenantId, null, "cachingAI");
      }
    }
    setUpdatedAt(mapper.selectLastUpdatedAt("tenants"));
  }

  /**
   * テーブルのLockをかける
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param tableInfo
   * @param tenantId
   * @return
   */
  @Transactional(transactionManager = "luigi2TransactionManager", readOnly = true)
  public String getLockTable(TableInfo tableInfo, Integer tenantId, Object updatedBy) {
    val map = Luigi2TableInfo.getLockTable(tableInfo);
    String code = numberingMapper.selectIncrementNumber(map, tenantId);
    numberingMapper.pessimisticLockKey(map, tenantId, code);
    if (tableInfo == TableInfo.Contracts || tableInfo == TableInfo.MaintenanceRequests
        || tableInfo == TableInfo.ClaimTrxsId) {
      numberingMapper.updateNo(map, tenantId, code, updatedBy);
      numberingMapper.insertNo(map, tenantId, code, updatedBy);
    }
    return code;
  }
  
  /**
   * 採番初期化更新日取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-25
   * @updatedAt : 2021-10-25
   * @return
   */
  public Date getUpdatedAt() {
    return updatedAt;
  }
  
  /**
   * 採番初期化更新日更新
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-25
   * @updatedAt : 2021-10-25
   * @return
   */
  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
  
  
}
