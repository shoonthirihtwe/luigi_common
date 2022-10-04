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
  private static SimpleDateFormat slashFormatter = new SimpleDateFormat("yyyy/MM/dd");
  private static SimpleDateFormat formatterIncludeTime = new SimpleDateFormat("yyyyMMddHHmm");

  /**
   * 日付取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-13
   * @updatedAt : 2022-04-13
   * @param date
   * @return
   * @throws ParseException
   */
  public static Date getDate(String date) throws ParseException {
    return date == null ? null : formatter.parse(date);
  }

  /**
   * yyyyMMdd形式で日付を返す
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-09-02
   * @updatedAt : 2021-09-02
   * @param date
   * @return
   */
  public static String convertSimpleFormat(Date date) {
    return date == null ? null : formatter.format(date);
  }

  /**
   * yyyyMMddHHmm形式で日付を返す
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/09/08
   * @updatedAt : 2022/09/08
   * @param date
   * @return
   */
  public static String convertSimpleFormatIncludeTime(Date date) {
    return date == null ? null : formatterIncludeTime.format(date);
  }

  /**
   * yyyyMMdd形式をyyyy-MM-dd形式に変換
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/10/04
   * @updatedAt : 2022/10/04
   * @param date
   * @return
   * @throws ParseException
   */
  public static String convertSlashFormat(String date) throws ParseException {

    return date == null ? null : slashFormatter.format(formatter.parse(date));
  }

  /**
   * 日取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-15
   * @updatedAt : 2022-04-15
   * @param date
   * @return
   */
  public static int getDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_MONTH);
  }

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

  /**
   * covert localdate to Date
   * 
   * @param localdate
   * @return
   */
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

  /**
   * convert date to local date
   * 
   * @param dateToConvert
   * @return
   */
  public static LocalDate convertDateToLocalDate(Date dateToConvert) {
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * 時間を削除する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-20
   * @updatedAt : 2021-07-20
   * @param date
   * @return
   */
  public static Date getDateRemoveTime(Date date) {
    Calendar calCur = Calendar.getInstance();
    calCur.setTime(date);
    calCur.set(Calendar.HOUR_OF_DAY, 0);
    calCur.set(Calendar.MINUTE, 0);
    calCur.set(Calendar.SECOND, 0);
    calCur.set(Calendar.MILLISECOND, 0);

    return calCur.getTime();
  }
}
