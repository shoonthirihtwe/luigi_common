package jp.co.ichain.luigi2.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import jp.co.ichain.luigi2.dto.ResultWebDto;
import jp.co.ichain.luigi2.resources.TenantResources;
import jp.co.ichain.luigi2.util.ControllerUtils;

/**
 * 共通API
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-09-01
 * @updatedAt : 2021-09-01
 */
@Controller
public class CommonController {

  @Autowired
  TenantResources tenantResources;

  @Autowired
  ControllerUtils controllerUtils;

  /**
   * テナントRefresh
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-09-01
   * @updatedAt : 2021-09-01
   * @param request
   * @return
   * @throws Exception
   */
  @Produces(MediaType.APPLICATION_JSON)
  @RequestMapping(value = "/s/initTenant", method = {RequestMethod.GET})
  public @ResponseBody ResultWebDto initTenant(HttpServletRequest request) throws Exception {
    tenantResources.initialize();
    ResultWebDto result = new ResultWebDto();
    result.setCode("OK");
    return result;
  }
}
