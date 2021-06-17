package jp.co.ichain.luigi2.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * 各種コード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-26
 * @updatedAt : 2021-05-26
 */
public class Luigi2TableInfo {
  public enum TableInfo {
    Contracts, Customers
  }

  private static final Map<TableInfo, Map<String, Object>> TABLE_INFO_MAP;
  
  static {
    TABLE_INFO_MAP = new HashMap<TableInfo, Map<String, Object>>();
    // contracts
    var map = new HashMap<String, Object>();
    map.put("table", "contracts");
    map.put("key", "contract_no");
    map.put("size", 10);
    TABLE_INFO_MAP.put(TableInfo.Contracts, map);

    // customers
    map = new HashMap<String, Object>();
    map.put("table", "customers");
    map.put("key", "customer_id");
    map.put("size", 12);
    TABLE_INFO_MAP.put(TableInfo.Customers, map);

  }

  /**
   * Lockを掛けたいテーブルを取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-02
   * @updatedAt : 2021-06-02
   * @param tableInfo
   * @return
   */
  public static Map<String, Object> getLockTable(TableInfo tableInfo) {
    return TABLE_INFO_MAP.get(tableInfo);
  }
}
