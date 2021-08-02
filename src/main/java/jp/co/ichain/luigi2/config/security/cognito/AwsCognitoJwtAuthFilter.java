package jp.co.ichain.luigi2.config.security.cognito;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.nimbusds.jwt.proc.BadJWTException;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;

/**
 * AwsCognitoJwtAuthFilter
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@SuppressWarnings("serial")
public class AwsCognitoJwtAuthFilter extends GenericFilter {

  private AwsCognitoIdTokenProcessor cognitoIdTokenProcessor;

  public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor cognitoIdTokenProcessor) {
    this.cognitoIdTokenProcessor = cognitoIdTokenProcessor;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    Authentication authentication;

    try {
      authentication = this.cognitoIdTokenProcessor.authenticate((HttpServletRequest) request);
      if (authentication != null) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        SecurityContextHolder.clearContext();
      }
    } catch (BadJWTException e) {
      response.getWriter().print("{\"code\":\"" + Luigi2ErrorCode.A0002 + "\"}");
      response.setContentType("application/json");
      SecurityContextHolder.clearContext();
      e.printStackTrace();
      return;

    } catch (Exception e) {
      response.getWriter().print("{\"code\":\"" + Luigi2ErrorCode.A0001 + "\"}");
      response.setContentType("application/json");
      SecurityContextHolder.clearContext();
      e.printStackTrace();
      return;
    }

    filterChain.doFilter(request, response);
  }
}
