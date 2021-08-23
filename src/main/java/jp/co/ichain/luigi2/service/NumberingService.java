package jp.co.ichain.luigi2.service;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
      if (numberingMapper
          .selectIncrementNumber(Luigi2TableInfo.getLockTable(TableInfo.Contracts),
              tenantId) == null) {
        numberingMapper.insertNo(Luigi2TableInfo.getLockTable(TableInfo.Contracts), tenantId, null,
            "cachingAI");
      }
      if (numberingMapper
          .selectIncrementNumber(Luigi2TableInfo.getLockTable(TableInfo.MaintenanceRequests),
              tenantId) == null) {
        numberingMapper.insertNo(Luigi2TableInfo.getLockTable(TableInfo.MaintenanceRequests),
            tenantId, null, "cachingAI");
      }
    }
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
    if (tableInfo == TableInfo.Contracts || tableInfo == TableInfo.MaintenanceDocuments) {
      numberingMapper.updateNo(map, tenantId, code, updatedBy);
      numberingMapper.insertNo(map, tenantId, code, updatedBy);
    }
    return code;
  }
}
