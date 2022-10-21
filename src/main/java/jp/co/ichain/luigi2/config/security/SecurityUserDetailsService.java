package jp.co.ichain.luigi2.config.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.service.AuthService;
import jp.co.ichain.luigi2.vo.UsersVo;
import lombok.val;

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

  @SuppressWarnings("unused")
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username == null || username.isEmpty()) {
      throw new UsernameNotFoundException("username is empty");
    }
    UsersVo userVo = new UsersVo();
    String[] tmp = username.split("::");
    userVo.setTenantId(Integer.parseInt(tmp[0]));
    userVo.setId(Integer.parseInt(tmp[1]));
    userVo.setLastLoginAt(new Date(Long.parseLong(tmp[2])));
    if (userVo != null) {
      SecurityUserDetails userDetails = new SecurityUserDetailsImpl();
      val roles = authService.getUserRole(userVo);
      if (roles != null) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (val role : roles) {
          grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        userDetails.setUser(userVo);
      } else {
        throw new UsernameNotFoundException(username + "is not found");
      }

      return (UserDetails) userDetails;
    }
    throw new UsernameNotFoundException(username + "is not found");
  }
}
