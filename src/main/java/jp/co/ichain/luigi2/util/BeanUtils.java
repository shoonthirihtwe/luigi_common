package jp.co.ichain.luigi2.util;

import org.springframework.context.ApplicationContext;
import jp.co.ichain.luigi2.config.ApplicationContextProvider;

/**
 * Bean Util
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-10
 * @updatedAt : 2021-06-10
 */
public class BeanUtils {

  /**
   * Bean nameでBean参照
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param beanName
   * @return
   */
  public static Object getBean(String beanName) {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    return applicationContext.getBean(beanName);
  }

  /**
   * Bean Groupを取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-10
   * @updatedAt : 2021-06-10
   * @param annotationCls
   * @return
   */
  public static Object[] getBeanByAnnotation(Class<?> annotationCls) {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    return applicationContext.getBeanNamesForAnnotation(null);
  }
}
