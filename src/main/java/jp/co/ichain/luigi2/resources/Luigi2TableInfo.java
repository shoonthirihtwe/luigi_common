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
    Contracts
  }

  private static final Map<TableInfo, Map<String, Object>> TABLE_INFO_MAP;
  private static final Map<String, Object> CONTRACTS_MAP;
  static {
    CONTRACTS_MAP = new HashMap<String, Object>();

    // contracts
    CONTRACTS_MAP.put("table", "contracts");
    CONTRACTS_MAP.put("key", "contract_no");
    CONTRACTS_MAP.put("size", 10);

    // customers
    CONTRACTS_MAP.put("table", "customers");
    CONTRACTS_MAP.put("key", "customer_id");
    CONTRACTS_MAP.put("size", 12);

    TABLE_INFO_MAP = new HashMap<TableInfo, Map<String, Object>>();
    TABLE_INFO_MAP.put(TableInfo.Contracts, CONTRACTS_MAP);
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
