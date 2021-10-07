package jp.co.ichain.luigi2.resources;

/**
 * 各種コード
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-26
 * @updatedAt : 2021-05-26
 */
public class Luigi2ErrorMessage {

  /**
   * ParameterErrorMessage
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   */
  public enum ErrorMessage {
    S0001("システムエラーが発生しました。"),
    V0000("%sが不正です。"), 
    V0001("%sが未入力です。"),
    V0002("%sの桁数が%d未満です。"), 
    V0003("%sの桁数が%d超過です。"),
    V0004("%sの書式が不正です。"), 
    V0005("%sの属性が不正です。"),
    V0006("%sの書式型が不正です。"), 
    V0007("%sのが現時刻を超えています。"),
    V0008("%sのみでは不正です。"), 
    V0009("%sの値が不正です。"),
    V0010("変更した顧客の必須項目が入力されていません。"),
    V0011("キーパラメータ%sが中腹されています。"),
    D0001("%sが一致しません。"),
    D0002("該当する%sが存在しません。"),
    D0003("システムエラー:%sの採番に失敗しました。"),
    D0004("%sの登録情報が不正です。"),
    D0005("別のアカウントで更新されました。もう一度画面を見直してください。");
    String val;

    ErrorMessage(String val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return this.val;
    }
  }
}
