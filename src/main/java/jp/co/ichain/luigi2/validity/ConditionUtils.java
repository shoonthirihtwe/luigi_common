package jp.co.ichain.luigi2.validity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebConditionException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.util.BeanUtils;
import lombok.val;

/**
 * 認証サービス
 * 
 * @author : [AOT] s.park
 * @createdAt : 2021-03-05
 * @updatedAt : 2021-03-05
 */
@Component
public class ConditionUtils {

  Map<String, Object> methodConditionMap;
  Map<String, Method> methodMap;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  @Autowired
  BeanUtils beanUtils;

  @PostConstruct
  void initialize() {
    methodConditionMap = new HashMap<String, Object>();
    methodMap = new HashMap<String, Method>();

    for (val condition : beanUtils.getBeanByAnnotation(Condition.class)) {
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
   */
  @SuppressWarnings("unchecked")
  public List<WebException> validateCondition(String souceKey, Integer tenantId, Object data)
      throws JsonMappingException, JsonProcessingException {
    // ServiceInstance 取得
    val serviceInstance = serviceInstancesBaseResources.get(tenantId).get(souceKey).get(0);
    if (serviceInstance == null) {
      return null;
    }
    val conditionMap = serviceInstance.getInherentMap();
    if (conditionMap == null || conditionMap.size() == 0) {
      return null;
    }
    List<WebException> result = null;

    for (val conditionMethod : conditionMap.keySet()) {
      Map<String, Object> argsMap = (Map<String, Object>) conditionMap.get(conditionMethod);
      try {
        if (validate(conditionMethod, tenantId, data,
            (List<Object>) argsMap.get("args")) == false) {
          if (result == null) {
            result = new ArrayList<WebException>();
          }
          val errArgs = (List<Object>) argsMap.get("errArgs");
          if (errArgs != null && errArgs.size() > 0) {
            result.add(new WebConditionException((String) argsMap.get("errCode"), errArgs));
          } else {
            result.add(new WebConditionException((String) argsMap.get("errCode")));
          }
        }
      } catch (Exception e) {
        result.add(new WebConditionException((String) argsMap.get("errCode")));
      }
    }
    return result;
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
    val condition = methodConditionMap.get(methodName);
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
      methodConditionMap.put(methodName, condition);
      methodMap.put(methodName, method);
    }
  }
}
