package jp.co.ichain.luigi2.util;

import java.util.Objects;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.dto.ResultWebDto;
import jp.co.ichain.luigi2.vo.ObjectVo;
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
  public interface ControllerFunction<T, U, W, R> {
    R apply(T t, U u, W w) throws Exception;

    default <V> ControllerFunction<T, U, W, V> andThen(Function<? super R, ? extends V> after)
        throws Exception {
      Objects.requireNonNull(after);
      return (T t, U u, W w) -> after.apply(apply(t, u, w));
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
  public ControllerFunction<HttpServletRequest, ObjectVo, ThrowingSupplier<ResultWebDto>, ? extends ResultWebDto> makeCommonControllerHandler()
      throws Exception {
    return (request, vo, supplier) -> {
      this.controllerFunction(request, vo);
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
  public ControllerFunction<HttpServletRequest, ObjectVo, ThrowingSupplier<ResponseEntity<Resource>>, ResponseEntity<Resource>> makeResourceControllerHandler()
      throws Exception {
    return (request, vo, supplier) -> {
      this.controllerFunction(request, vo);
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
  private void controllerFunction(HttpServletRequest request, ObjectVo vo) throws Exception {}

}
