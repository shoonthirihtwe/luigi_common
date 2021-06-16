package jp.co.ichain.luigi2.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Web プロジェクトなのか検証
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-16
 * @updatedAt : 2021-06-16
 */
public class WebExistsCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment env = context.getEnvironment();
    return env.containsProperty("server.port");
  }
}
