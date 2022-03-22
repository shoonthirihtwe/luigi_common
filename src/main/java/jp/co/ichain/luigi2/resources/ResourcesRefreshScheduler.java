package jp.co.ichain.luigi2.resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.ichain.luigi2.config.WebExistsCondition;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.service.NumberingService;
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
@Conditional(WebExistsCondition.class)
public class ResourcesRefreshScheduler {

  @Autowired
  CommonMapper mapper;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

  @Autowired
  CodeMasterResources codeMasterResources;

  @Autowired
  ValidityResources validityResources;

  @Autowired
  FrontDesignResources frontDesignResources;

  @Autowired
  NumberingService numberingService;

  @Autowired
  CacheManager cacheManager;

  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  @SuppressWarnings("rawtypes")
  @Autowired
  RedisTemplate redisTemplate;

  /**
   * リソースを更新する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-13
   * @updatedAt : 2021-08-13
   */
  @Scheduled(cron = "${config.refresh-scheduler.cron}")
  public void cronJobSch() {

    val updatedAtResult = mapper.selectResourcesLastUpdatedAt();
    updatedAtResult.forEach(item -> {
      val tenantId = ((Long) item.get("tenantId")).intValue();
      if (tenantId == -1) {
        try {
          if (numberingService.getUpdatedAt() == null
              || numberingService.getUpdatedAt().getTime() < (long) item.get("updatedAt")) {
            numberingService.initialize();
            numberingService.setUpdatedAt(new Date((long) item.get("updatedAt")));
            System.out.println(
                "[NumberingService Refresh] NumberingService Refresh : " + sdf.format(new Date()));
          }
        } catch (InstantiationException | IllegalAccessException | SecurityException e) {
          e.printStackTrace();
        }
      } else {
        try {
          val updatedAt = serviceInstancesResources.getUpdatedAt(tenantId);
          if (updatedAt == null || updatedAt.getTime() < (long) item.get("updatedAt")) {

            // clear tenant redis data
            val conn = redisTemplate.getConnectionFactory().getConnection();
            Set<byte[]> keys = conn.keys(("*::" + tenantId).getBytes());
            for (val n : keys) {
              conn.del(n);
            }

            serviceInstancesResources.initialize(tenantId);
            codeMasterResources.initialize(tenantId);
            validityResources.initialize(tenantId);
            frontDesignResources.initialize(tenantId);
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
