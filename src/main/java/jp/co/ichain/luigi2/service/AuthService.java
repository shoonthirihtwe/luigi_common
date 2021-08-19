package jp.co.ichain.luigi2.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.config.security.SecurityUserDetails;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.UserMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.vo.AuthoritiesVo;
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

  @Autowired
  UserMapper userMapper;

  @Value("${env.debug.mode}")
  Boolean isDebugMode;

  /**
   * ログイン中の会員取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-02
   * @updatedAt : 2021-06-02
   * @return
   */
  public UsersVo getCurrentUser() {
    // TODO: g.kim テストのため、後で削除予定 
    if (isDebugMode) {
      val result = new UsersVo();
      result.setTenantId(1);
      result.setId(1);
      result.setEmail("test@aot.co.jp");
      result.setSub("a1234");
      result.setLastLoginAt(new Date());
      return result;
    }

    try {
      SecurityUserDetails userDetails =
          (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
      return userDetails.getCurrentUser();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * ログイン
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-28
   * @updatedAt : 2021-07-28
   * @return
   */
  public List<AuthoritiesVo> loginUser(UsersVo userVo) {

    if (userMapper.updateLoginUser(userVo) == 0) {
      throw new WebException(Luigi2ErrorCode.D0004, "account");
    }

    val authorities = userMapper.getLoginUserAuth(userVo);

    return authorities;
  }

  /**
   * Admin権限取得
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-28
   * @updatedAt : 2021-07-28
   * @return
   */
  public List<AuthoritiesVo> getAdminAuth(UsersVo userVo) {

    val authorities = userMapper.getAdminAuth(userVo);

    return authorities;
  }
}
