package jp.co.ichain.luigi2.util;

import java.util.Collections;
import java.util.List;

/**
 * リスト操作ユーティリティー
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class CollectionUtils {
  /**
   * Foreach文使用時、Nullエラーを防ぐ
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param <T>
   * @param iterable
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] safe(T[] iterable) {
    return iterable == null ? (T[]) new Object[0] : iterable;
  }

  /**
   * Foreach文使用時、Nullエラーを防ぐ
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param <T>
   * @param iterable
   * @return
   */
  public static <T> Iterable<T> safe(Iterable<T> iterable) {
    return iterable == null ? Collections.<T>emptyList() : iterable;
  }

  /**
   * Foreach文使用時、Nullエラーを防ぐ
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param data
   * @param defaultValue
   * @return
   */
  public static Object safe(Object data, Object defaultValue) {
    if (data == null) {
      return defaultValue;
    } else {
      return data;
    }
  }

  /**
   * リストが空か検証する
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param list
   * @return
   */
  public static boolean isEmpty(List<?> list) {
    if (list == null || list.size() == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * リストが空か検証する
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param <T>
   * @param list
   * @return
   */
  public static <T> boolean isEmpty(T[] list) {
    if (list == null || list.length < 1) {
      return true;
    } else {
      return false;
    }
  }
}
