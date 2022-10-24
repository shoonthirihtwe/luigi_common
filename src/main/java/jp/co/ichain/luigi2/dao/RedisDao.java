package jp.co.ichain.luigi2.dao;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.val;

@Service
public class RedisDao {

  @SuppressWarnings("rawtypes")
  @Autowired
  RedisTemplate redisTemplate;

  /**
   * clear redis data
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-10-24
   * @updatedAt : 2022-10-24
   * @param
   * @return
   */
  public void clearRedisData(Integer tenantId) {
    RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
    Set<byte[]> keys = conn.keys(("*::*" + tenantId + "*").getBytes());
    for (val n : keys) {
      conn.del(n);
    }
  }

  /**
   * clear redis data
   *
   * @author : [AOT] g.kim
   * @createdAt : 2022-10-24
   * @updatedAt : 2022-10-24
   * @param
   * @return
   */
  public void clearRedisData() {
    RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
    Set<byte[]> keys = conn.keys(("*::*").getBytes());
    for (val n : keys) {
      conn.del(n);
    }
  }
}
