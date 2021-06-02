package jp.co.ichain.luigi2.service;

import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.vo.UsersVo;
import lombok.val;

/**
 * 認証サービス
 * 
 * @author : [AOT] s.park
 * @createdAt : 2021-03-05
 * @updatedAt : 2021-03-05
 */
@Service
public class AuthService {


  /**
   * ログイン中の会員取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-02
   * @updatedAt : 2021-06-02
   * @return
   */
  public UsersVo getCurrentUser() {
    val result = new UsersVo();
    result.setTenantId(1);
    return result;
  }
}
