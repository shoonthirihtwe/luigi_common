package jp.co.ichain.luigi2.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.service.AuthService;
import jp.co.ichain.luigi2.vo.UsersVo;

/**
 * ユーザー情報サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

  @Autowired
  private AuthService authService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username == null || username.isEmpty()) {
      throw new UsernameNotFoundException("username is empty");
    }
    UsersVo userVo = new UsersVo();
    String[] tmp = username.split("::");
    userVo.setTenantId(Integer.parseInt(tmp[0]));
    userVo.setId(Integer.parseInt(tmp[1]));
    // TODO g.kim ログイン情報取得
    // userVo.setKind(tmp[2]);
    try {
      // TODO g.kim ログイン情報取得
      // userVo = authDao.getUser(userVo);
      userVo = authService.getCurrentUser();
    } catch (Exception e) {
      throw new UsernameNotFoundException(username + "is not found");
    }
    if (userVo != null) {
      // TODO g.kim ログイン情報取得
      // userVo.setAccessId(tmp[3]);
      SecurityUserDetails userDetails = new SecurityUserDetailsImpl();
      userDetails.setUser(userVo);
      return (UserDetails) userDetails;
    }
    throw new UsernameNotFoundException(username + "is not found");
  }
}
