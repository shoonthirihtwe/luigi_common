package jp.co.ichain.luigi2.config.security.cognito;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import jp.co.ichain.luigi2.vo.AuthoritiesVo;
import jp.co.ichain.luigi2.vo.TenantsVo;
import jp.co.ichain.luigi2.vo.UsersVo;
import lombok.val;

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

  @Value("${env.debug.mode}")
  Boolean isDebugMode;

  @Value("${external.api.flag}")
  Boolean isExternalApi;
  
  @Value("${external.api.roles}")
  String externalApiRoles;

  public Authentication authenticate(HttpServletRequest request) throws Exception {

    List<AuthoritiesVo> authorities = null;
    UsersVo userVo = null;

    // テストのためのログイン認証
    // idTokenがあればその認証情報に上書きされる
    if (isDebugMode) {
      userVo = authService.getCurrentUser();
      authorities = authService.getAdminAuth(userVo);
    }

    TenantsVo tenantsVo = null;

    // 外部APIの場合、Cognito認証をSkip
    if (isExternalApi) {
      tenantsVo = authService.getExternalApiTenants(request.getHeader("x-api-key"));
      if (tenantsVo == null) {
        return null;
      }
      userVo = new UsersVo();
      userVo.setTenantId(tenantsVo.getId());
      userVo.setId(1);
      userVo.setLastLoginAt(tenantsVo.getOnlineDate());
      authorities = authService.getApiAuth(userVo, externalApiRoles);
    } else {
      String domain = request.getHeader("x-frontend-domain");

      if (domain == null) {
        return null;
      }

      if (domain.indexOf(":") == -1) {
        tenantsVo = tenantResources.get(domain);
      } else {
        tenantsVo = tenantResources.get(request.getHeader("x-frontend-domain").split(":")[0]);
      }

      String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
      if (idToken != null) {
        String bearerToken = this.getBearerToken(idToken);
        if (bearerToken == null) {
          return null;
        }
        configurableJwtProcessor =
            beanFactory.getBean(CognitoConfig.class).configurableJwtProcessor(tenantsVo.getId());

        JWTClaimsSet claims = this.configurableJwtProcessor.process(bearerToken, null);
        validateIssuer(claims, tenantsVo.getId());
        verifyIfIdToken(claims, tenantsVo.getId());
        val username = getUserNameFrom(claims);
        if (username != null) {
          userVo = new UsersVo();
          userVo.setSub(username);
          userVo.setTenantId(tenantsVo.getId());
          userVo.setLastLoginAt(tenantsVo.getOnlineDate());
          authorities = authService.loginUser(userVo);
        }
      }
    }

    if (authorities != null) {
      List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
      for (val authority : authorities) {
        grantedAuthorities.add(new SimpleGrantedAuthority(authority.getFunctionId()));
      }

      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
          String.valueOf(userVo.getTenantId() + "::" + userVo.getId() + "::"
              + userVo.getLastLoginAt().getTime()),
          userVo.getSub(), grantedAuthorities);
      SecurityUserDetails userDetails = new SecurityUserDetailsImpl();
      userDetails.setUser(userVo);
      token.setDetails(userDetails);
      return token;
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
