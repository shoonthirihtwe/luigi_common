package jp.co.ichain.luigi2.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @Value("${external.api.flag}")
  Boolean isExternalApi;

  @Value("${business.group.type}")
  String businessGroupType;

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
   * Throwing Supplier
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @param <T>
   */
  @FunctionalInterface
  public interface ThrowingSupplierInParameterMap<T> {
    T get(Map<String, Object> map) throws Exception;
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
      commonService.validate(paramMap, endpoint);
      val result = supplier.get();

      result.setCode("OK");
      return result;
    };
  }

  public ControllerFunction<HttpServletRequest, String, Map<String, Object>, ThrowingSupplier<ResultWebDto>, ? extends ResultWebDto> makeNotValidationControllerHandler()
      throws Exception {
    return (request, endpoint, paramMap, supplier) -> {
      this.controllerFunction(request, endpoint, paramMap);
      val result = supplier.get();

      result.setCode("OK");
      return result;
    };
  }

  /**
   * File upload時
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-31
   * @updatedAt : 2021-05-31
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public ControllerFunction<HttpServletRequest, String, MultipartRequest, ThrowingSupplierInParameterMap<ResultWebDto>, ? extends ResultWebDto> makeFileControllerHandler()
      throws Exception {
    return (request, endpoint, mulr, supplier) -> {
      val paramMap = new HashMap<String, Object>();
      val requestMap = request.getParameterMap();

      if (mulr != null) {
        // File
        val fileMap = mulr.getMultiFileMap();
        for (val key : CollectionUtils.safe(fileMap.keySet())) {
          val list = fileMap.get(key);
          if (key.contains("List")) {
            paramMap.put(key, list);
          } else if (list != null && list.size() > 0) {
            paramMap.put(key, list.get(0));
          }
        }
      }

      // Data
      for (String key : CollectionUtils.safe(requestMap.keySet())) {
        if ("json".equals(key)) {
          val json = requestMap.get(key);
          if (json != null && json.length > 0) {
            ObjectMapper mapper = new ObjectMapper();
            paramMap.putAll(mapper.readValue(json[0], Map.class));
          }
          continue;
        }
        val list = requestMap.get(key);
        if (key.contains("List")) {
          paramMap.put(key, Arrays.asList(list));
        } else if (list != null && list.length > 0) {
          paramMap.put(key, list[0]);
        }
      }
      this.controllerFunction(request, endpoint, paramMap);
      commonService.validate(paramMap, endpoint);
      val result = supplier.get(paramMap);

      result.setCode("OK");
      return result;
    };
  }

  /**
   * Get時
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-05
   * @updatedAt : 2021-07-05
   * @param request
   * @param endpoint
   * @param supplier
   * @return
   * @throws Exception
   */
  public ResultWebDto makeGetControllerHandler(HttpServletRequest request, String endpoint,
      ThrowingSupplierInParameterMap<ResultWebDto> supplier) throws Exception {
    return makeFileControllerHandler().apply(request, endpoint, null, supplier);
  }

  /**
   * 返還タイプがResponseEntity場合の処理
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
      commonService.validate(paramMap, endpoint);
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

    // tenant setting
    val curUser = authService.getCurrentUser();
    TenantsVo tenantVo = null;
    if (curUser != null) {
      tenantVo = tenantResources.get(curUser.getTenantId());
      paramMap.put("tenantId", tenantVo.getId());
      if (isExternalApi) {
        paramMap.put("updatedBy", endpoint);
      } else {
        paramMap.put("updatedBy", curUser.getId());
      }
      paramMap.put("onlineDate", tenantVo.getOnlineDate());
      paramMap.put("batchDate", tenantVo.getBatchDate());
    } else {
      String domain = request.getHeader("x-frontend-domain");
      if (domain.indexOf(":") == -1) {
        tenantVo = tenantResources.get(domain);
      } else {
        tenantVo = tenantResources.get(request.getHeader("x-frontend-domain").split(":")[0]);
      }

      if (tenantVo != null) {
        paramMap.put("tenantId", tenantVo.getId());
        paramMap.put("onlineDate", tenantVo.getOnlineDate());
        paramMap.put("batchDate", tenantVo.getBatchDate());
      }
    }

    // page setting
    if (paramMap.get("page") != null) {
      var rowCount = paramMap.get("rowCount");
      var page = paramMap.get("page");
      if (rowCount instanceof String) {
        rowCount = Integer.valueOf((String) rowCount);
        paramMap.put("rowCount", rowCount);
      }
      if (page instanceof String) {
        page = Integer.valueOf((String) page);
      }
      if (rowCount == null) {
        rowCount = 50;
        paramMap.put("rowCount", rowCount);
      }
      paramMap.put("page", (((int) page) - 1) * (int) rowCount);
    }

    // businessGroupType settting
    paramMap.put("businessGroupType", businessGroupType);
  }
}
