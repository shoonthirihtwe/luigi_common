package jp.co.ichain.luigi2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@PropertySource("classpath:common/application-common.properties")
public class Luigi2Application {

  public static void main(String[] args) {
    SpringApplication.run(Luigi2Application.class, args);
  }

}
