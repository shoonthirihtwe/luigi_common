package jp.co.ichain.luigi2.config.security;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * SameSite=Noneを設定する
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@Service
public class SamesiteNoneFilter implements Filter {
  static final String ORIGIN = "Origin";

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = ((HttpServletResponse) res);
    Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
    boolean firstHeader = true;
    // there can be multiple Set-Cookie attributes
    for (String header : headers) {
      if (firstHeader) {
        response.setHeader(HttpHeaders.SET_COOKIE,
            String.format("%s; %s", header, "SameSite=None"));
        firstHeader = false;
        continue;
      }
      response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=None"));
    }
    chain.doFilter(req, res);
  }
}
