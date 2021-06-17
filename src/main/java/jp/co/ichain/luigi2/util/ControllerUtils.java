package jp.co.ichain.luigi2.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.dto.ResultWebDto;
import jp.co.ichain.luigi2.resources.TenantResources;
import jp.co.ichain.luigi2.service.AuthService;
import jp.co.ichain.luigi2.service.CommonService;
import jp.co.ichain.luigi2.vo.TenantsVo;
import lombok.val;

/**
 * Controller共通処理
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Service
public class ControllerUtils {

  @Autowired
  AuthService authService;

  @Autowired
  CommonService commonService;

  @Autowired
  TenantResources tenantResources;

  /**
   * Controller Function
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param <T>
   * @param <U>
   * @param <W>
   * @param <R>
   */
  @FunctionalInterface
  public interface ControllerFunction<T, U, W, Y, R> {
    R apply(T t, U u, W w, Y y) throws Exception;

    default <V> ControllerFunction<T, U, W, Y, V> andThen(Function<? super R, ? extends V> after)
        throws Exception {
      Objects.requireNonNull(after);
      return (T t, U u, W w, Y y) -> after.apply(apply(t, u, w, y));
    }
  }

  /**
   * Throwing Supplier
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param <T>
   */
  @FunctionalInterface
  public interface ThrowingSupplier<T> {
    T get() throws Exception;
  }

  /**
   * Controller 設定など
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   * @throws Exception
   */
  public ControllerFunction<HttpServletRequest, String, Map<String, Object>, ThrowingSupplier<ResultWebDto>, ? extends ResultWebDto> makeCommonControllerHandler()
      throws Exception {
    return (request, endpoint, paramMap, supplier) -> {
      this.controllerFunction(request, endpoint, paramMap);
      val result = supplier.get();

      result.setCode("OK");
      return result;
    };
  }

  /**
   * Controller 設定など
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   * @throws Exception
   */
  public ControllerFunction<HttpServletRequest, String, Map<String, Object>, ThrowingSupplier<ResponseEntity<Resource>>, ResponseEntity<Resource>> makeResourceControllerHandler()
      throws Exception {
    return (request, endpoint, paramMap, supplier) -> {
      this.controllerFunction(request, endpoint, paramMap);
      val result = supplier.get();

      return result;
    };
  }

  /**
   * Controller 設定など
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param request
   * @param vo
   * @throws Exception
   */
  private void controllerFunction(HttpServletRequest request, String endpoint,
      Map<String, Object> paramMap) throws Exception {

    val curUser = authService.getCurrentUser();
    if (curUser != null) {
      paramMap.put("tenantId", curUser.getTenantId());
      paramMap.put("updatedBy", curUser.getId());
    } else {
      String domain = request.getHeader("x-frontend-domain");
      TenantsVo tenantVo = null;
      if (domain.indexOf(":") == -1) {
        tenantVo = tenantResources.get(domain);
      } else {
        tenantVo = tenantResources.get(request.getHeader("x-frontend-domain").split(":")[0]);
      }

      if (tenantVo != null) {
        paramMap.put("tenantId", tenantVo.getId());
      }
    }
    commonService.validate(paramMap, endpoint);
  }
}
