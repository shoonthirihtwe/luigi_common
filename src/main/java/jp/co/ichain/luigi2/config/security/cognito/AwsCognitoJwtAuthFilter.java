package jp.co.ichain.luigi2.config.security.cognito;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
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
      ((HttpServletResponse) response).sendError(HttpStatus.SC_UNAUTHORIZED);
      // TODO エラーコード生成
      response.getWriter().print("{\"code\":\"" + Luigi2ErrorCode.S0000 + "\"}");
      response.setContentType("application/json");
      SecurityContextHolder.clearContext();
      e.printStackTrace();
      return;
    } catch (Exception e) {
      ((HttpServletResponse) response).sendError(HttpStatus.SC_UNAUTHORIZED);
      // TODO エラーコード生成
      response.getWriter().print("{\"code\":\"" + Luigi2ErrorCode.S0000 + "\"}");
      response.setContentType("application/json");
      SecurityContextHolder.clearContext();
      e.printStackTrace();
      return;
    }

    filterChain.doFilter(request, response);
  }
}
