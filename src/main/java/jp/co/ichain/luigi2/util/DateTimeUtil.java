package jp.co.ichain.luigi2.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
  public static String convertDateToYearMonth(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    return date == null ? null
        : formatter.format(date).substring(0, 5);
  }
}
