package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * Gmo Service
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
@Service
public class PaymentService {

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

  private static Map<String, String> API_URL_MAP = new HashMap<String, String>() {
    private static final long serialVersionUID = -4307678510471312599L;
    {
      put("EntryTran", "https://pt01.mul-pay.jp/payment/EntryTran.idPass");
      put("ExecTran", "https://pt01.mul-pay.jp/payment/ExecTran.idPass");
      put("SearchCard", "https://pt01.mul-pay.jp/payment/SearchCard.idPass");
      put("RegistMember", "https://pt01.mul-pay.jp/payment/SaveMember.idPass");
      put("RegistCard", "https://pt01.mul-pay.jp/payment/SaveCard.idPass");
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
  public GmoPaymentVo pay(String contractNo, String cardCustNumber, Date dueDate,
      Integer premiumDueAmount) throws IllegalArgumentException, IllegalAccessException,
      GmoPaymentException, IOException, ParseException {
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
    return execTran(gmoPaymentVo);
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
