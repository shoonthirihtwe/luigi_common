package jp.co.ichain.luigi2.service.pay.gmo;

import java.io.IOException;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.util.GmoPaymentApiUtils;
import jp.co.ichain.luigi2.util.Params;
import jp.co.ichain.luigi2.vo.FactoringCompaniesVo;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;


@Service
public class GmoPaymentBankAccountDelegate implements GmoPaymentDelegate {

  @Value("${gmo.entry.tran.bank.account}")
  private String entryTranBankAccount;

  @Value("${gmo.exec.tran.bank.account}")
  private String execTranBankAccount;

  @Value("${gmo.bank.account.cancel}")
  private String bankAccountCancel;

  /**
   * 口座振替決済実行
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-13
   * @updatedAt : 2022-04-13
   * @param gmoPaymentVo
   * @param companiesVo
   * @return
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws ParseException
   */
  @Override
  public GmoPaymentVo execTran(GmoPaymentVo gmoPaymentVo, FactoringCompaniesVo companiesVo)
      throws GmoPaymentException,
      IllegalArgumentException, IllegalAccessException, IOException, ParseException {

    // 取引ID取得
    GmoPaymentVo entryTranVo = this.entryTranBankAccount(gmoPaymentVo, companiesVo);

    // 取引IDセット
    gmoPaymentVo.setAccessID(entryTranVo.getAccessID());
    gmoPaymentVo.setAccessPass(entryTranVo.getAccessPass());

    // GMO 決済実行API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(execTranBankAccount, "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    apiResult.setAccessID(gmoPaymentVo.getAccessID());
    apiResult.setAccessPass(gmoPaymentVo.getAccessPass());
    return apiResult;
  }

  /**
   * 取引消し
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-06
   * @updatedAt : 2022-04-06
   * @param gmoPaymentVo
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  @Override
  public GmoPaymentVo cancel(GmoPaymentVo gmoPaymentVo, Object orderId)
      throws IllegalArgumentException, IllegalAccessException, GmoPaymentException, IOException,
      ParseException {

    // orderId setting
    gmoPaymentVo.setOrderID((String) orderId);
    // GMO 決済実行API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(bankAccountCancel, "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    apiResult.setAccessID(gmoPaymentVo.getAccessID());
    apiResult.setAccessPass(gmoPaymentVo.getAccessPass());
    return apiResult;
  }

  /**
   * 口座振替取引登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-13
   * @updatedAt : 2022-04-13
   * @param gmoPaymentVo
   * @param companiesVo
   * @return
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws ParseException
   */
  private GmoPaymentVo entryTranBankAccount(GmoPaymentVo gmoPaymentVo,
      FactoringCompaniesVo companiesVo) throws GmoPaymentException, IllegalArgumentException,
      IllegalAccessException, IOException, ParseException {

    // 振替指定日
    gmoPaymentVo.setTargetDate("27");

    // 請求内容
    gmoPaymentVo.setRemarks(companiesVo.getPassbookEntry());
    // パラメーターセット
    Params params = new Params(gmoPaymentVo, true);

    // GMO 取引ID取得API呼び出す
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(entryTranBankAccount, "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    return apiResult;
  }
}
