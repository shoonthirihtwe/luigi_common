package jp.co.ichain.luigi2.resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import lombok.val;

/**
 * リソースを更新するScheduler
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-13
 * @updatedAt : 2021-08-13
 */
@Singleton
@Component
public class ResourcesRefreshScheduler {

  @Autowired
  CommonMapper mapper;

  @Autowired
  TenantResources tenantResources;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

  @Autowired
  CodeMasterResources codeMasterResources;

  @Autowired
  ValidityResources validityResources;

  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  /**
   * リソースを更新する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-13
   * @updatedAt : 2021-08-13
   */
  @Scheduled(cron = "0 0 * * * *")
  public void cronJobSch() {
    val updatedAtResult = mapper.selectResourcesLastUpdatedAt();
    updatedAtResult.forEach(item -> {
      val tenantId = ((Long) item.get("tenantId")).intValue();
      if (tenantId == -1) {
        // tenantResources
        try {
          if (tenantResources.updatedAt.getTime() < (long) item.get("updatedAt")) {
            tenantResources.initialize();
            System.out.println("[Resources Refresh] tenantResources Refresh : " + tenantId + " "
                + sdf.format(new Date()));
          }
        } catch (InstantiationException | IllegalAccessException | SecurityException e) {
          e.printStackTrace();
        }
      } else {
        // serviceInstancesResources
        try {
          val updatedAt = serviceInstancesResources.updatedAtMap.get(tenantId);
          if (updatedAt
              .getTime() < (long) item.get("updatedAt")) {
            serviceInstancesResources.initialize(tenantId);
            codeMasterResources.initialize(tenantId);
            validityResources.initialize(tenantId);
            System.out.println("[Resources Refresh] serviceInstancesResources Refresh : " + tenantId
                + " " + sdf.format(new Date()));
          }
        } catch (SecurityException | JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    });

  }
}
