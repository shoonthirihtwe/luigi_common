package jp.co.ichain.luigi2.validity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.util.BeanUtils;
import lombok.val;

/**
 * 変換サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
@Component
public class ConversionUtils {

  Map<String, Object> methodConversionMap;
  Map<String, Method> methodMap;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  @Autowired
  BeanUtils beanUtils;

  @PostConstruct
  void initialize() {
    methodConversionMap = new HashMap<String, Object>();
    methodMap = new HashMap<String, Method>();

    for (val conversion : beanUtils.getBeanByAnnotation(Conversion.class)) {
      settingConversion(conversion);
    }
  }

  /**
   * 自動変換を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @param methodName
   * @param tenantId
   * @param data
   * @return
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  Object convert(String methodName, Integer tenantId, Object data)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    val conversion = methodConversionMap.get(methodName);
    val method = methodMap.get(methodName);

    return method.invoke(conversion, data, tenantId);
  }


  /**
   * Methodをセットする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @param conversion
   */
  private void settingConversion(Object conversion) {
    for (val method : conversion.getClass().getMethods()) {
      val methodName = method.getName();
      methodConversionMap.put(methodName, conversion);
      methodMap.put(methodName, method);
    }
  }
}
