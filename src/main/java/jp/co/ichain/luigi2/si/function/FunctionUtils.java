package jp.co.ichain.luigi2.si.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.util.BeanUtils;
import lombok.val;

/**
 * 共通関数サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-16
 * @updatedAt : 2021-07-16
 */
@Component
public class FunctionUtils {


  Map<String, Object> methodFunctionMap;
  Map<String, Method> methodMap;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  @Autowired
  BeanUtils beanUtils;

  @PostConstruct
  void initialize() {
    methodFunctionMap = new HashMap<String, Object>();
    methodMap = new HashMap<String, Method>();

    for (val condition : beanUtils.getBeanByAnnotation(Function.class)) {
      settingCondition(condition);
    }
  }

  /**
   * バッチなどで使用する固有検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-15
   * @updatedAt : 2021-07-15
   * @param souceKey
   * @param tenantId
   * @param data
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public Object get(String souceKey, String functionMethod, Integer tenantId, Object... params)
      throws JsonMappingException, JsonProcessingException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    // ServiceInstance 取得
    val serviceInstance = serviceInstancesBaseResources.get(tenantId).get(souceKey).get(0);
    if (serviceInstance == null) {
      return null;
    }
    val conditionMap = serviceInstance.getInherentMap();
    if (conditionMap == null || conditionMap.size() == 0) {
      return null;
    }
    val func = conditionMap.get(functionMethod);
    if (func == null) {
      return null;
    }

    val condition = methodFunctionMap.get(func);
    val method = methodMap.get(func);

    return method.invoke(condition, tenantId, params);
  }

  /**
   * 共通条件部検証を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param methodName
   * @param data
   * @param args
   * @return
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  boolean validate(String methodName, Integer tenantId, Object data, List<Object> args)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    val condition = methodFunctionMap.get(methodName);
    val method = methodMap.get(methodName);

    return (boolean) method.invoke(condition, data, tenantId, args);
  }


  /**
   * Methodをセットする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param condition
   */
  private void settingCondition(Object condition) {
    for (val method : condition.getClass().getMethods()) {
      val methodName = method.getName();
      methodFunctionMap.put(methodName, condition);
      methodMap.put(methodName, method);
    }
  }
}
