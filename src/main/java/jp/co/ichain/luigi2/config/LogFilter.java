package jp.co.ichain.luigi2.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (event.getMessage().contains("==NOT_SQL_LOG==")) {
      return FilterReply.DENY;
    } else {
      return FilterReply.NEUTRAL;
    }
  }

}
