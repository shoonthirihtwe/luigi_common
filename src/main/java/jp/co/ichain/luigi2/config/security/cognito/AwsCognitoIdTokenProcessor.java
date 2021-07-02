package jp.co.ichain.luigi2.config.security.cognito;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import jp.co.ichain.luigi2.config.security.SecurityUserDetails;
import jp.co.ichain.luigi2.config.security.SecurityUserDetailsImpl;
import jp.co.ichain.luigi2.resources.TenantResources;
import jp.co.ichain.luigi2.service.AuthService;
import jp.co.ichain.luigi2.vo.UsersVo;

/**
 * AwsCognitoIdTokenProcessor
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
// @Slf4j
@Component
public class AwsCognitoIdTokenProcessor {

  @Autowired
  private JwtConfiguration jwtConfiguration;

  @Autowired
  private BeanFactory beanFactory;

  private ConfigurableJWTProcessor<SecurityContext> configurableJwtProcessor;

  @Autowired
  private AuthService authService;

  @Autowired
  TenantResources tenantResources;

  public Authentication authenticate(HttpServletRequest request) throws Exception {

    String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
    if (idToken != null) {
      String bearerToken = this.getBearerToken(idToken);
      if (bearerToken == null) {
        return null;
      }

      String domain = request.getHeader("x-frontend-domain");
      Integer tenantId = null;
      if (domain.indexOf(":") == -1) {
        tenantId = tenantResources.get(domain).getId();
      } else {
        tenantId =
            tenantResources.get(request.getHeader("x-frontend-domain").split(":")[0]).getId();
      }

      configurableJwtProcessor =
          beanFactory.getBean(CognitoConfig.class).configurableJwtProcessor(tenantId);

      JWTClaimsSet claims = this.configurableJwtProcessor.process(bearerToken, null);
      validateIssuer(claims, tenantId);
      verifyIfIdToken(claims, tenantId);
      String username = getUserNameFrom(claims);
      if (username != null) {
        UsersVo userVo = new UsersVo();
        userVo.setEmail(username);
        userVo.setTenantId(tenantId);

        // TODO login UserInfo
        userVo = authService.getCurrentUser();
        // userVo.setSub(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("TODO権限"));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            "TODOUserName", "TODOPassword", grantedAuthorities);
        SecurityUserDetails userDetails = new SecurityUserDetailsImpl();
        userDetails.setUser(userVo);
        token.setDetails(userDetails);
        return token;
      }
    }
    return null;
  }

  private String getUserNameFrom(JWTClaimsSet claims) {
    return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
  }

  private void verifyIfIdToken(JWTClaimsSet claims, int tenantId) throws Exception {
    if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl(tenantId))) {
      throw new Exception("JWT Token is not an ID Token");
    }
  }

  private void validateIssuer(JWTClaimsSet claims, int tenantId) throws Exception {
    if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl(tenantId))) {
      throw new Exception(String.format("Issuer %s does not match cognito idp %s",
          claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl(tenantId)));
    }
  }

  private String getBearerToken(String token) {
    return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : null;
  }
}
