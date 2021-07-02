package jp.co.ichain.luigi2.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.google.gson.Gson;
import jp.co.ichain.luigi2.dto.ResultOneDto;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.vo.UsersVo;

/**
 * 要請が何らかの理由でSecurityに拒否された時、リターン値を設定。
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    String result = "";
    ResultOneDto<UsersVo> resultMakeDto = new ResultOneDto<UsersVo>();
    resultMakeDto.setCode(Luigi2ErrorCode.D0004);

    Gson gson = new Gson();
    result = gson.toJson(resultMakeDto);

    response.getWriter().print(result);
    response.getWriter().flush();
  }
}
