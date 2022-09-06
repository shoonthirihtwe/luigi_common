package jp.co.ichain.luigi2.routine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jp.co.ichain.luigi2.mapper.RoutineMapper;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.util.BeanUtils;
import jp.co.ichain.luigi2.util.CollectionUtils;
import lombok.val;

/**
 * 固有ロジック Utils
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2022-05-24
 * @updatedAt : 2022-05-24
 */
@Component
public class RoutineUtils {

  Map<String, Object> methodConditionMap;
  Map<String, Method> methodMap;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  @Autowired
  BeanUtils beanUtils;

  @Autowired
  RoutineMapper routineMapper;

  @PostConstruct
  void initialize() {
    methodConditionMap = new HashMap<String, Object>();
    methodMap = new HashMap<String, Method>();

    for (val condition : beanUtils.getBeanByAnnotation(Routine.class)) {
      settingCondition(condition);
    }
  }

  /**
   * ルーチンを実行する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-05-24
   * @updatedAt : 2022-05-24
   * @param methodName
   * @param params
   * @return
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public boolean executionRoutine(String routineLabel, Map<String, Object> param)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    val siParam = new HashMap<String, Object>();
    siParam.put("tenantId", param.get("tenantId"));
    siParam.put("businessGroupType", param.get("businessGroupType"));
    siParam.put("routineLabel", routineLabel);

    val siList = routineMapper.selectServiceInstances(siParam);
    for (val si : CollectionUtils.safe(siList)) {
      val methodName = si.get("source_key");
      val condition = methodConditionMap.get(methodName);
      val method = methodMap.get(methodName);
      if ((boolean) method.invoke(condition, param) == false) {
        return false;
      }
    }

    return true;
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
