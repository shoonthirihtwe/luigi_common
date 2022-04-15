package jp.co.ichain.luigi2.service.pay;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.service.pay.gmo.GmoPaymentDelegate;
import jp.co.ichain.luigi2.service.pay.gmo.GmoPaymentProperties;
import jp.co.ichain.luigi2.vo.BillingDetailsVo;
import jp.co.ichain.luigi2.vo.FactoringCompaniesVo;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;
import jp.co.ichain.luigi2.vo.PaymentErrorVo;
import jp.co.ichain.luigi2.vo.PaymentVo;
import lombok.val;

/**
 * Gmo Service
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
class GmoPaymentService {

  public GmoPaymentDelegate delegate;

  public GmoPaymentService(GmoPaymentDelegate delegate) {
    this.delegate = delegate;
  }

  private SimpleDateFormat systemDateForamt = new SimpleDateFormat("yyMMddHHmm");
  Map<String, String> ERROR_MAP = GmoPaymentProperties.getInstance().ERROR_MAP;

  /**
   * 決済実行
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param cardCustNumber
   * @param contractNo
   * @param dueDate
   * @param premiumDueAmount
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  PaymentVo pay(FactoringCompaniesVo companyInfo, BillingDetailsVo billingDetailsVo, Date nowDate)
      throws IllegalArgumentException,
      IllegalAccessException, GmoPaymentException, IOException, ParseException {
    GmoPaymentVo gmoPaymentVo = new GmoPaymentVo();
    Date now = new Date();

    // contracts.card_cust_number
    gmoPaymentVo.setMemberID(billingDetailsVo.getCardCustNumber());
    /*
     * 証券番号billing_details.contract_no + 決済処理日(システム日付yymmdd) + システム時刻(hhmm) +
     * 充当月billing_details.due_date(yyyymm) を設定
     */
    gmoPaymentVo.setOrderID(billingDetailsVo.getContractNo() + systemDateForamt.format(now)
        + billingDetailsVo.getDueDate());

    // 保険料請求額billing_details.premium_due_amountを設定する
    gmoPaymentVo.setAmount((long) billingDetailsVo.getPremiumDueAmount());

    // サイトID、PASSセット
    gmoPaymentVo.setSiteID(companyInfo.getSiteId());
    gmoPaymentVo.setSitePass(companyInfo.getSitePass());
    // ショップ情報セット
    gmoPaymentVo.setShopID(companyInfo.getShopId());
    gmoPaymentVo.setShopPass(companyInfo.getShopPass());

    // 決済実行
    GmoPaymentVo exeResult =
        delegate.execTran(gmoPaymentVo, companyInfo, billingDetailsVo, nowDate);
    return new PaymentVo(exeResult.getAccessID(), exeResult.getAccessPass(), now,
        changePaymentErrorInfo(exeResult.getErrorMap()));
  }

  /**
   * 決済取り消し
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-06
   * @updatedAt : 2022-04-06
   * @param companyInfo
   * @param accessId
   * @param accessPassword
   * @param suspenceDate
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  public PaymentVo cancel(FactoringCompaniesVo companyInfo, String accessId,
      String accessPassword, Object suspenceDate) throws IllegalArgumentException,
      IllegalAccessException, GmoPaymentException, IOException, ParseException {

    Date now = new Date();
    // 取引IDセット
    GmoPaymentVo gmoPaymentVo = new GmoPaymentVo();
    gmoPaymentVo.setAccessID(accessId);
    gmoPaymentVo.setAccessPass(accessPassword);

    // サイトID、PASSセット
    gmoPaymentVo.setSiteID(companyInfo.getSiteId());
    gmoPaymentVo.setSitePass(companyInfo.getSitePass());

    val exeResult = delegate.cancel(gmoPaymentVo, suspenceDate);
    return new PaymentVo(exeResult.getAccessID(), exeResult.getAccessPass(), now,
        changePaymentErrorInfo(exeResult.getErrorMap()));
  }

  /**
   * 共通エラー変換
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-28
   * @updatedAt : 2021-06-28
   * @param errMap
   * @return
   */
  List<PaymentErrorVo> changePaymentErrorInfo(Map<String, PaymentErrorVo> errMap) {
    List<PaymentErrorVo> result = null;
    // 共通決済エラーに変換
    if (errMap != null && errMap.size() > 0) {
      result = new ArrayList<PaymentErrorVo>();
      for (val errorVo : errMap.values()) {
        result.add(new PaymentErrorVo(ERROR_MAP.get(errorVo.getErrCode()), errorVo.getErrInfo()));
      }
    }

    return result;
  }


}
