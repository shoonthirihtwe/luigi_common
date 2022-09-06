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

  /**
   * カタカナ→ひらがな
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/08/30
   * @updatedAt : 2022/08/30
   * @param str
   * @return
   */
  public static String convertHiraToKata(String str) {
    if (str == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < str.length(); i++) {
      char code = str.charAt(i);
      if ((code >= 0x30a1) && (code <= 0x30f3)) {
        buf.append((char) (code - 0x60));
      } else {
        buf.append(code);
      }
    }
    return new String(buf);
  }

  /**
   * ハイフン付き郵便番号作成
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022/08/30
   * @updatedAt : 2022/08/30
   * @param str
   * @return
   */
  public static String makeHyphenZipcode(String str) {
    if (str == null || str.length() != 7) {
      return str;
    }

    return str.substring(0, 3) + "-" + str.substring(3, 7);
  }
}
