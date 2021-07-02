package jp.co.ichain.luigi2.config.security;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jp.co.ichain.luigi2.vo.UsersVo;

/**
 * Security用会員POJO
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
public class SecurityUserDetailsImpl implements UserDetails, SecurityUserDetails {
  /**
   * 
   */
  private static final long serialVersionUID = -8317427330941497560L;

  private UsersVo userVo;
  private Collection<GrantedAuthority> authorities;

  @Override
  public void setUser(UsersVo userVo) {
    this.userVo = userVo;
    this.authorities = new ArrayList<GrantedAuthority>();
    // TODO g.kim 権限設定
    this.authorities.add(new SimpleGrantedAuthority("TODO権限"));
  }

  @Override
  public UsersVo getCurrentUser() {
    return this.userVo;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    // TODO g.kim ログイン情報
    // return "{noop}" + this.userVo.getAccessId();
    return "{noop}";
  }

  @Override
  public String getUsername() {
    return String.valueOf(userVo.getTenantId() + "::" + userVo.getId());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
