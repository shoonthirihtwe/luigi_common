package jp.co.ichain.luigi2.util;

import java.math.BigDecimal;

/**
 * 数値ツール
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-10-15
 * @updatedAt : 2021-10-15
 */
public class NumberUtils {

  /**
   * 整数取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-10-15
   * @updatedAt : 2021-10-15
   * @param number
   * @return
   */
  public static long getLong(Object number) {
    if (number == null) {
      return 0;
    }
    return (long) number;
  }

  /**
   * 実数取得F
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-10-15
   * @updatedAt : 2021-10-15
   * @param number
   * @return
   */
  public static double getDoubleToBigDecimal(Object number) {
    if (number == null) {
      return 0;
    }
    return ((BigDecimal) number).doubleValue();
  }
}
