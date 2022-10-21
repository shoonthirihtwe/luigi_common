package jp.co.ichain.luigi2.config.security;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import jp.co.ichain.luigi2.resources.AuthoritiesResources;
import lombok.val;

/**
 * リクエスト権限チェック
 *
 * @author : [AOT] g.kim
 * @createdAt : 2022-10-21
 * @updatedAt : 2022-10-21
 */
@Component
public class AuthorizationChecker {

  @Autowired
  private AuthoritiesResources authoritiesResources;

  public boolean check(HttpServletRequest request, Authentication authentication) {

    SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getDetails();
    val roles = authentication.getAuthorities().stream().map(vo -> vo.getAuthority())
        .collect(Collectors.toList());
    try {
      val functionList = authoritiesResources
          .getAuthorityFunctionIds(userDetails.getCurrentUser().getTenantId(), roles, true);
      for (val functionId : functionList) {
        if (new AntPathMatcher().match("/**/" + functionId, request.getRequestURI())) {
          return true;
        }
      }
    } catch (Exception e) {
      return false;
    }

    return false;
  }
}
