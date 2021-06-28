package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.util.GmoPaymentApiUtils;
import jp.co.ichain.luigi2.util.Params;
import jp.co.ichain.luigi2.util.StringUtils;
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
@Service
class GmoPaymentService {

  @Value("${gmo.site-id}")
  private String siteId;

  @Value("${gmo.site-pass}")
  private String sitePass;

  @Value("${gmo.shop-id}")
  private String shopId;

  @Value("${gmo.shop-pass}")
  private String shopPass;

  private SimpleDateFormat systemDateForamt = new SimpleDateFormat("yyyyMMddHHmm");
  private SimpleDateFormat dueDateForamt = new SimpleDateFormat("yyyyMM");

  @SuppressWarnings("serial")
  private static Map<String, String> API_URL_MAP = new HashMap<String, String>() {
    {
      put("EntryTran", "https://pt01.mul-pay.jp/payment/EntryTran.idPass");
      put("ExecTran", "https://pt01.mul-pay.jp/payment/ExecTran.idPass");
      put("SearchCard", "https://pt01.mul-pay.jp/payment/SearchCard.idPass");
      put("RegistMember", "https://pt01.mul-pay.jp/payment/SaveMember.idPass");
      put("RegistCard", "https://pt01.mul-pay.jp/payment/SaveCard.idPass");
      put("AlterTran", "https://pt01.mul-pay.jp/payment/AlterTran.idPass");
    }
  };

  @SuppressWarnings("serial")
  private static Map<String, String> ERROR_MAP = new HashMap<String, String>() {
    {
      put("42G020000", "G02");
      put("42G030000", "G03");
      put("42G040000", "G04");
      put("42G050000", "G05");
      put("42G070000", "G07");
      put("42G120000", "G12");
      put("42G220000", "G22");
      put("42G300000", "G30");
      put("42G540000", "G54");
      put("42G550000", "G55");
      put("42G560000", "G56");
      put("42G600000", "G60");
      put("42G610000", "G61");
      put("42G830000", "G83");
      put("42G920000", "G92");
      put("42G950000", "G95");
      put("42G960000", "G96");
      put("42G970000", "G97");
      put("42G980000", "G98");
      put("42G990000", "G99");
    }
  };

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
  PaymentVo pay(String contractNo, String cardCustNumber, Date dueDate, Integer premiumDueAmount)
      throws IllegalArgumentException, IllegalAccessException, GmoPaymentException, IOException,
      ParseException {
    GmoPaymentVo gmoPaymentVo = new GmoPaymentVo();
    Date now = new Date();

    // contracts.card_cust_number
    gmoPaymentVo.setMemberID(cardCustNumber);
    /*
     * 証券番号billing_details.contract_no + 決済処理日(システム日付yymmdd) + システム時刻(hhmm) +
     * 充当月billing_details.due_date(yyyymm) を設定
     */
    gmoPaymentVo
        .setOrderID(contractNo + systemDateForamt.format(now) + dueDateForamt.format(dueDate));

    // 保険料請求額billing_details.premium_due_amountを設定する
    gmoPaymentVo.setAmount((long) premiumDueAmount);

    List<GmoPaymentVo> list = searchCard(gmoPaymentVo);
    if (list == null || list.size() < 1) {
      throw new WebException("");
    }
    GmoPaymentVo cardVo = list.get(0);

    // カード情報セット
    gmoPaymentVo.setCardSeq(cardVo.getCardSeq());
    // 1：一括
    gmoPaymentVo.setMethod(1);
    // CAPTURE：即時売上
    gmoPaymentVo.setJobCd("CAPTURE");

    // 決済実行
    GmoPaymentVo exeResult = execTran(gmoPaymentVo);
    return new PaymentVo(exeResult.getAccessID(), exeResult.getAccessPass(), now,
        changePaymentErrorInfo(exeResult.getErrorMap()));
  }

  /**
   * 取引消し
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-28
   * @updatedAt : 2021-06-28
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
  PaymentVo cancel(String accessId, String accessPassword, Date suspenceDate)
      throws IllegalArgumentException, IllegalAccessException, GmoPaymentException, IOException,
      ParseException {
    // 取引IDセット
    GmoPaymentVo gmoPaymentVo = new GmoPaymentVo();
    gmoPaymentVo.setAccessID(accessId);
    gmoPaymentVo.setAccessPass(accessPassword);

    // サイトID、PASSセット
    gmoPaymentVo.setSiteID(siteId);
    gmoPaymentVo.setSitePass(sitePass);

    // Job設定
    Calendar now = Calendar.getInstance();
    Calendar suspence = Calendar.getInstance();
    suspence.setTime(suspenceDate);

    if (now.get(Calendar.YEAR) == suspence.get(Calendar.YEAR)
        && now.get(Calendar.MONTH) == suspence.get(Calendar.MONTH)) {
      gmoPaymentVo.setJobCd("RETURN");
    } else {
      gmoPaymentVo.setJobCd("RETURNX");
    }

    // GMO 決済実行API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(API_URL_MAP.get("AlterTran"), "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    apiResult.setAccessID(gmoPaymentVo.getAccessID());
    apiResult.setAccessPass(gmoPaymentVo.getAccessPass());

    return new PaymentVo(apiResult.getAccessID(), apiResult.getAccessPass(), now.getTime(),
        changePaymentErrorInfo(apiResult.getErrorMap()));
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

  /**
   * 取引登録
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param gmoPaymentVo
   * @return
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws ParseException
   */
  private GmoPaymentVo entryTran(GmoPaymentVo gmoPaymentVo) throws GmoPaymentException,
      IllegalArgumentException, IllegalAccessException, IOException, ParseException {

    // ショップ情報セット
    gmoPaymentVo.setShopID(shopId);
    gmoPaymentVo.setShopPass(shopPass);
    gmoPaymentVo.setTdFlag(0);

    // パラメーターセット
    Params params = new Params(gmoPaymentVo, true);

    // GMO 取引ID取得API呼び出す
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(API_URL_MAP.get("EntryTran"), "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    return apiResult;
  }

  /**
   * 決済実行
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param gmoPaymentVo
   * @return
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws ParseException
   */
  private GmoPaymentVo execTran(GmoPaymentVo gmoPaymentVo) throws GmoPaymentException,
      IllegalArgumentException, IllegalAccessException, IOException, ParseException {

    // 取引ID取得
    GmoPaymentVo entryTranVo = this.entryTran(gmoPaymentVo);

    // 取引IDセット
    gmoPaymentVo.setAccessID(entryTranVo.getAccessID());
    gmoPaymentVo.setAccessPass(entryTranVo.getAccessPass());

    // サイトID、PASSセット
    gmoPaymentVo.setSiteID(siteId);
    gmoPaymentVo.setSitePass(sitePass);

    // GMO 決済実行API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(API_URL_MAP.get("ExecTran"), "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    apiResult.setAccessID(gmoPaymentVo.getAccessID());
    apiResult.setAccessPass(gmoPaymentVo.getAccessPass());
    return apiResult;
  }

  /**
   * カードリスト取得
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-22
   * @updatedAt : 2021-06-22
   * @param gmoPaymentVo
   * @return
   * @throws GmoPaymentException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws ParseException
   */
  private List<GmoPaymentVo> searchCard(GmoPaymentVo gmoPaymentVo) throws GmoPaymentException,
      IllegalArgumentException, IllegalAccessException, IOException, ParseException {
    // サイトID、PASSセット
    gmoPaymentVo.setSiteID(siteId);
    gmoPaymentVo.setSitePass(sitePass);

    // GMO カード情報取得API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(API_URL_MAP.get("SearchCard"), "POST",
        params.getParams(), "windows-31j", "windows-31j");

    // GMO カード情報パッシング
    List<GmoPaymentVo> result = new ArrayList<GmoPaymentVo>();
    if (StringUtils.isEmpty(apiResult.getCardSeq()) == false) {
      String[] cardSeqs = apiResult.getCardSeq().split("\\|");
      String[] cardNos = apiResult.getCardNo().split("\\|");
      String[] expires = apiResult.getExpire().split("\\|");
      for (int i = 0; i < cardSeqs.length; i++) {
        GmoPaymentVo vo = new GmoPaymentVo();
        vo.setCardSeq(cardSeqs[i]);
        vo.setCardNo(cardNos[i]);
        vo.setExpire(expires[i]);
        result.add(vo);
      }
    }

    return result;
  }
}
