package jp.co.ichain.luigi2.resources.code;

/**
 * 通知内容
 * 
 * @author : [VJP] タン
 * @createdAt : 2021-08-25
 * @updatedAt : 2021-08-25
 */
public class Luigi2CodeNotifications {

  /**
   * 通知方法
   * 
   * 00:メール
   * 
   * @author : [VJP] タン
   * @createdAt : 2021-08-25
   * @updatedAt : 2021-08-25
   */
  public enum NotificationMethod {
    EMAIL("00");

    String val;

    NotificationMethod(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
