package jp.co.ichain.luigi2.advice;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ログ出力
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-27
 * @updatedAt : 2021-05-27
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

  /** publicメソッドのポイントカット. システムエラーログ出力 */
  @Pointcut("execution(* jp.co.ichain.luigi2..*.*(..))")
  private void publicLog() {}

  /** Controllerアノテーションのポイントカット. */
  @Pointcut("@within(org.springframework.stereotype.Controller)")
  private void controllerLog() {}

  /** RestControllerアノテーションのポイントカット. */
  @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
  private void restControllerLog() {}

  /** Serviceアノテーションのポイントカット. */
  @Pointcut("@within(org.springframework.stereotype.Service)")
  private void serviceLog() {}

  /**
   * メソッド実行前にログ出力
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param jp
   */
  @Before("publicLog() && (restControllerLog() || controllerLog() || serviceLog())")
  public void doBefore(JoinPoint jp) {
    val sig = jp.getSignature();
    log.info("【操作開始】クラス名：" + jp.getTarget().getClass().toString() + "." + "メソッド："
        + sig.getDeclaringTypeName());
  }

  /**
   * メソッド実行後にログ出力
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param jp
   * @param returnValue
   */
  @AfterReturning(
      pointcut = "publicLog() && (restControllerLog() || controllerLog() || serviceLog())",
      returning = "returnValue")
  public void doAfterReturning(JoinPoint jp, Object returnValue) {
    val sig = jp.getSignature();
    log.info("【操作終了】クラス名：" + jp.getTarget().getClass().toString() + "." + "メソッド："
        + sig.getDeclaringTypeName() + "." + sig.getName() + "#戻り値：" + returnValue);
  }

  /**
   * エラーが発生した場合、ログ出力
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param jp
   * @param ex
   */
  @AfterThrowing(
      pointcut = "publicLog() && (restControllerLog() || controllerLog() || serviceLog())",
      throwing = "ex")
  public void doAfterThrowing(JoinPoint jp, Exception ex) {
    val sig = jp.getSignature();
    val sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    log.info("【エラー】クラス名：" + jp.getTarget().getClass().toString() + "." + "メソッド："
        + sig.getDeclaringTypeName() + "." + sig.getName() + "#エラーメッセージ：" + sw.toString());
  }
}
