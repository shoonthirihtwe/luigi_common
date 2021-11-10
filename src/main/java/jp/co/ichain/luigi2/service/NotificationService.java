package jp.co.ichain.luigi2.service;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.ichain.luigi2.dao.AwsMailDao;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.mapper.NotificationMapper;
import jp.co.ichain.luigi2.resources.Luigi2ReceiverEmailInfo.MailType;
import jp.co.ichain.luigi2.resources.Luigi2ReceiverEmailInfo.ReceiverInfo;
import jp.co.ichain.luigi2.util.CollectionUtils;
import lombok.val;

/**
 * 通知サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-16
 * @updatedAt : 2021-07-16
 */
@Service
public class NotificationService {

  @Autowired
  CommonMapper commonMapper;

  @Autowired
  NotificationMapper mapper;

  @Autowired
  AwsMailDao mailDao;

  /**
   * 通知登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-16
   * @updatedAt : 2021-07-16
   * @param param {tenantId, contractNo, notificationDate, updatedBy 必須)
   *        comment : 通信欄コメント
   *        sendee : 通知対象者 「セットしない場合、契約者名が自動でセットされる」
   *        email : 通知対象者 「セットしない場合、契約者Emailが自動でセットされる」
   *        noitificationMethod : 通知方法「セットしない場合、00でセットされる」
   * @return
   */
  @Transactional(transactionManager = "luigi2TransactionManager", rollbackFor = Exception.class)
  public void registerNotification(String templateNumber, Map<String, Object> param,
      ReceiverInfo receiverInfo, MailType mailType, String... appendRegisterFieldNames) {

    // data (json) add
    Map<String, Object> appendMap = new HashMap<String, Object>();
    for (val appendField : CollectionUtils.safe(appendRegisterFieldNames)) {
      appendMap.put(appendField, param.get(appendField));
    }
    param.put("data", new JSONObject(appendMap));

    // templateNumber add
    param.put("templateNumber", templateNumber);

    // notificationMethod
    if (param.get("notificationMethod") == null) {
      param.put("notificationMethod", "00");
    }
    
    // sender設定
    if (param.get("emailSender") == null) {
      param.put("emailSender", mailDao.getSender(receiverInfo, mailType, param));
    }

    // 通知登録
    mapper.insertNotification(param);
  }

}
