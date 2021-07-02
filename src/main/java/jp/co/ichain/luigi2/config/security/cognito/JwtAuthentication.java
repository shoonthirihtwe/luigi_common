package jp.co.ichain.luigi2.config.security.cognito;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * JwtAuthentication
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@SuppressWarnings("serial")
public class JwtAuthentication extends AbstractAuthenticationToken {

  private final Object principal;
  private JWTClaimsSet jwtClaimsSet;

  public JwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.jwtClaimsSet = jwtClaimsSet;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public JWTClaimsSet getJwtClaimsSet() {
    return this.jwtClaimsSet;
  }
}
