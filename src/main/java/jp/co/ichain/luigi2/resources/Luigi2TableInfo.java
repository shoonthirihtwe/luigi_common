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

  private static final Map<TableInfo, Map<String, String>> TABLE_INFO_MAP;
  private static final Map<String, String> CONTRACTS_MAP;
  static {
    CONTRACTS_MAP = new HashMap<String, String>();
    CONTRACTS_MAP.put("table", "contracts");
    CONTRACTS_MAP.put("key", "contract_no");

    TABLE_INFO_MAP = new HashMap<TableInfo, Map<String, String>>();
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
  public static Map<String, String> getLockTable(TableInfo tableInfo) {
    return TABLE_INFO_MAP.get(tableInfo);
  }
}
