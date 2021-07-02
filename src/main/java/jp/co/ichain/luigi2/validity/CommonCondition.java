package jp.co.ichain.luigi2.validity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.util.BeanUtils;
import lombok.val;

/**
 * 認証サービス
 * 
 * @author : [AOT] s.park
 * @createdAt : 2021-03-05
 * @updatedAt : 2021-03-05
 */
@Service
public class CommonCondition {


  Map<String, Object> methodConditionMap;
  Map<String, Method> methodMap;

  @PostConstruct
  void initialize() {
    methodConditionMap = new HashMap<String, Object>();
    methodMap = new HashMap<String, Method>();

    for (val condition : BeanUtils.getBeanByAnnotation(Condition.class)) {
      settingCondition(condition);
    }
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
  public boolean validate(String methodName, Integer tenantId, Object data, List<Object> args)
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
