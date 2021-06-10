package jp.co.ichain.luigi2.validity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import lombok.val;

/**
 * 認証サービス
 * 
 * @author : [AOT] s.park
 * @createdAt : 2021-03-05
 * @updatedAt : 2021-03-05
 */
@Service
public class InherentCondition {

  Map<String, Condition> METHOD_CONDITION_MAP;
  Map<String, Method> METHOD_MAP;

  @PostConstruct
  void initialize() {
    METHOD_CONDITION_MAP = new HashMap<String, Condition>();
    METHOD_MAP = new HashMap<String, Method>();
  }

  /**
   * 共通条件部検証を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param methodName
   * @param args
   * @return
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public boolean validate(String methodName, Object data, List<Object> args)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    val condition = METHOD_CONDITION_MAP.get(methodName);
    val method = METHOD_MAP.get(methodName);

    return (boolean) method.invoke(condition, data, args);
  }


  /**
   * Methodを追加する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param condition
   */
  public void addCondition(Condition condition) {
    for (val method : condition.getClass().getMethods()) {
      val methodName = method.getName();
      METHOD_CONDITION_MAP.put(methodName, condition);
      METHOD_MAP.put(methodName, method);
    }
  }
}
