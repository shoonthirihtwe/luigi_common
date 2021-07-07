package jp.co.ichain.luigi2.util;

import java.lang.annotation.Annotation;
import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import jp.co.ichain.luigi2.config.ApplicationContextProvider;

/**
 * Bean Util
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-10
 * @updatedAt : 2021-06-10
 */
@Component
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
  public Object getBean(String beanName) {
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
  public Collection<Object> getBeanByAnnotation(Class<? extends Annotation> annotationCls) {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    return applicationContext.getBeansWithAnnotation(annotationCls).values();
  }
}
