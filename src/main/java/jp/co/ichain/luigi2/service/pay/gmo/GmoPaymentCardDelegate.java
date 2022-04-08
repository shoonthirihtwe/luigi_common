package jp.co.ichain.luigi2.service.pay.gmo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.util.GmoPaymentApiUtils;
import jp.co.ichain.luigi2.util.Params;
import jp.co.ichain.luigi2.util.StringUtils;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;


@Service
public class GmoPaymentCardDelegate implements GmoPaymentDelegate {

  @Value("${gmo.entry.tran}")
  private String entryTran;

  @Value("${gmo.exec.tran}")
  private String execTran;

  @Value("${gmo.search.card}")
  private String searchCard;

  @Value("${gmo.alter.tran}")
  private String alterTran;

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
  @Override
  public GmoPaymentVo execTran(GmoPaymentVo gmoPaymentVo) throws GmoPaymentException,
      IllegalArgumentException, IllegalAccessException, IOException, ParseException {

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

    // 取引ID取得
    GmoPaymentVo entryTranVo = this.entryTran(gmoPaymentVo);

    // 取引IDセット
    gmoPaymentVo.setAccessID(entryTranVo.getAccessID());
    gmoPaymentVo.setAccessPass(entryTranVo.getAccessPass());

    // GMO 決済実行API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(execTran, "POST",
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
  @Override
  public GmoPaymentVo cancel(GmoPaymentVo gmoPaymentVo, Object suspenceDate)
      throws IllegalArgumentException, IllegalAccessException, GmoPaymentException, IOException,
      ParseException {

    // Job設定
    Calendar now = Calendar.getInstance();
    Calendar suspence = Calendar.getInstance();
    suspence.setTime((Date) suspenceDate);

    if (now.get(Calendar.YEAR) == suspence.get(Calendar.YEAR)
        && now.get(Calendar.MONTH) == suspence.get(Calendar.MONTH)) {
      gmoPaymentVo.setJobCd("RETURN");
    } else {
      gmoPaymentVo.setJobCd("RETURNX");
    }

    // GMO 決済実行API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(alterTran, "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
    apiResult.setAccessID(gmoPaymentVo.getAccessID());
    apiResult.setAccessPass(gmoPaymentVo.getAccessPass());
    return apiResult;
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

    gmoPaymentVo.setTdFlag(0);

    // パラメーターセット
    Params params = new Params(gmoPaymentVo, true);

    // GMO 取引ID取得API呼び出す
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(entryTran, "POST",
        params.getParams(), "windows-31j", "windows-31j");

    if (apiResult.getErrorMap() != null) {
      throw new GmoPaymentException(apiResult);
    }
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

    // GMO カード情報取得API呼び出す
    Params params = new Params(gmoPaymentVo, true);
    GmoPaymentVo apiResult = GmoPaymentApiUtils.doHttpResult(searchCard, "POST",
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
