package jp.co.ichain.luigi2.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.mapper.CommonMapper;

/**
 * 営業日取得サービス
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-07-01
 * @updatedAt : 2021-07-01
 */
@Service
public class OpenDateService {

  @Autowired
  CommonMapper commonMapper;

  /**
   * 営業日取得
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param tenantId テナントID
   * @param date 基準日
   * @param count 何日後
   * @return openDate
   */
  public Date getOpenDate(Integer tenantId, Date date, Integer count) {
    // 営業日取得
    Date openDate = commonMapper.selectOpenDate(tenantId, date, count - 1);
    return openDate;
  }

}
