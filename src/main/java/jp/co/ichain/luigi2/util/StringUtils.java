package jp.co.ichain.luigi2.util;

/**
 * 文字列検査 Utils
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-15
 * @updatedAt : 2021-06-15
 */
public class StringUtils {

  /**
   * 文字列空き検証
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-15
   * @updatedAt : 2021-06-15
   * @param src
   * @return
   */
  public static boolean isEmpty(String src) {
    if (src == null || src.length() == 0) {
      return true;
    }
    return false;
  }
}
