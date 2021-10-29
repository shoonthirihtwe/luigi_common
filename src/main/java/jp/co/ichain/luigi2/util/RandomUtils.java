package jp.co.ichain.luigi2.util;

import java.util.Random;

/**
 * RandomUtils
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-10-28
 * @updatedAt : 2021-10-28
 */
public class RandomUtils {

  /**
   * ランダム文字列生成(URIで予約文字は除外)
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-10-28
   * @updatedAt : 2021-10-28
   * @param length
   * @return
   */
  public static String generateRandomUriStr(Integer length) {
    int leftLimit = 48;
    int rightLimit = 126;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i == 45) || (i == 46) || (i == 95) || (i == 126)
            || ((i <= 57 && i >= 48) || (i <= 90 && i >= 65) || (i <= 122 && i >= 97)))
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

  }
}
