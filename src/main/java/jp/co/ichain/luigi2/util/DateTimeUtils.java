package jp.co.ichain.luigi2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * DateTimeUtils
 * 
 * @author : [VJP] n.h.hoang
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
public class DateTimeUtils {

  private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

  /**
   * convert Date to String with format yyyyMM
   * 
   * @param date
   * @return
   */
  public static String convertDateToYearMonth(Date date) {
    return date == null ? null : formatter.format(date).substring(0, 6);
  }

  public static String convertDateToDay(Date date) {
    return date == null ? null : formatter.format(date).substring(6, 8);
  }

  /**
   * add month to Date(Date with format yyyyMM)
   * 
   * @param dateString
   * @param addMonth
   * @return
   * @throws ParseException
   */
  public static String addMonthToYearMonth(String dateString, int addMonth) {
    dateString += "01";
    LocalDate localDate = toDate(dateString);
    localDate = localDate.plusMonths(addMonth);
    return toString(localDate).substring(0, 6);
  }

  public static Date addDayToYearMonth(String yyyyMm, Integer dd) {
    yyyyMm += "01";
    LocalDate currentDate = toDate(yyyyMm);
    LocalDate nextDate = currentDate.plusDays(dd - 1);
    if (currentDate.getMonthValue() != nextDate.getMonthValue()) {
      return convertLocalDateToDate(nextDate.plusDays(1 - nextDate.getDayOfMonth()));
    }
    return convertLocalDateToDate(nextDate);
  }

  /**
   * convert String to LocalDate
   * 
   * @param date
   * @return
   */
  public static LocalDate toDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    return LocalDate.parse(date, formatter);
  }

  public static Date convertLocalDateToDate(LocalDate localdate) {
    // default time zone
    ZoneId defaultZoneId = ZoneId.systemDefault();

    // local date + atStartOfDay() + default time zone + toInstant() = Date
    return Date.from(localdate.atStartOfDay(defaultZoneId).toInstant());
  }

  /**
   * convert LocalDate to String
   * 
   * @param localDate
   * @return
   */
  public static String toString(LocalDate localDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String formattedString = localDate.format(formatter);
    return formattedString;
  }

  /**
   * get the first day of the month
   * 
   * @param date
   * @return String yyyyMMdd
   */
  public static String getFirstDayOfMonth(Date date) {
    return convertDateToYearMonth(date) + "01";
  }

  /**
   * get the last day of the month
   * 
   * @param date
   * @return String yyyyMMdd
   */
  public static String getLastDayOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.add(Calendar.DATE, -1);

    Date lastDayOfMonth = calendar.getTime();
    return formatter.format(lastDayOfMonth).substring(0, 8);

  }
}
