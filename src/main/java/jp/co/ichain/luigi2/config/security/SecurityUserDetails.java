package jp.co.ichain.luigi2.config.security;

import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.vo.UsersVo;

/**
 * Securityに会員詳細を格納用
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@Service
public interface SecurityUserDetails {

  /**
   * 現在ユーザーを取得する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @return
   */
  public UsersVo getCurrentUser();

  /**
   * 現在ユーザーをセットする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param userVo
   */
  public void setUser(UsersVo userVo);
}
