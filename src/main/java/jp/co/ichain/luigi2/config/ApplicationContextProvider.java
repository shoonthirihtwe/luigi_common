package jp.co.ichain.luigi2.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 手動Bean参照
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-10
 * @updatedAt : 2021-06-10
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    applicationContext = ctx;
  }

  public static void setApplicationContextFonConsole(ApplicationContext ctx) throws BeansException {
    applicationContext = ctx;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

}
