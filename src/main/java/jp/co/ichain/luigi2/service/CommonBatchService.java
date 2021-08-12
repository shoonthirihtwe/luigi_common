package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.mapper.CommonBatchMapper;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeBillingHeaders;
import jp.co.ichain.luigi2.util.DateTimeUtils;
import jp.co.ichain.luigi2.vo.BillingDetailVo;
import jp.co.ichain.luigi2.vo.BillingDetailsVo;
import jp.co.ichain.luigi2.vo.BillingHeaderVo;
import jp.co.ichain.luigi2.vo.ContractBillingVo;
import jp.co.ichain.luigi2.vo.ContractPremiumHeader;
import jp.co.ichain.luigi2.vo.DepositDetailsVo;
import jp.co.ichain.luigi2.vo.DepositHeadersVo;
import jp.co.ichain.luigi2.vo.PaymentVo;
import jp.co.ichain.luigi2.vo.PremiumHeadersVo;

/**
 * Batchサービス
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
@Service
public class CommonBatchService {

  @Autowired
  CommonBatchMapper mapper;

  @Autowired
  PaymentService paymentService;

  private SimpleDateFormat dueDateForamt = new SimpleDateFormat("yyyyMM");

  /**
   * 請求決済処理を実施し入金データを作成
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   * @throws ParseException
   */
  public void payAndCreateDepositData(List<BillingDetailsVo> billingDetails, int tenantId,
      Date batchDate, String createdBy) throws ParseException {

    // バッチナンバー
    int batchNo = mapper.selectBatchNo(batchDate);

    // GMOのバリデーションフラグ
    boolean validGmo = false;

    // インデックス
    int index = 0;

    // バッチ合計金額
    int batchTotalAmount = 0;

    // 入金金額
    int receivedAmount = 0;

    for (BillingDetailsVo billingDetail : billingDetails) {

      String contractNo = billingDetail.getContractNo(); // 証券番号
      String contractBranchNo = billingDetail.getContractBranchNo(); // 証券番号枝番
      String cardCustNumber = billingDetail.getCardCustNumber(); // カード登録顧客番号 ※証券番号または親証券番号
      String dueDate = billingDetail.getDueDate(); // 請求詳細の充当月
      Integer premiumDueAmount = billingDetail.getTotalGrossPremium(); // 合計保険料金額

      // GMO決済サービス
      PaymentVo paymentVo = new PaymentVo();
      String errMessage = ""; // エラーメッセージ
      String accessId = ""; // 取引ID
      String accessPass = ""; // 取引パスワード
      try {
        paymentVo = paymentService.pay(contractNo, cardCustNumber, dueDate, premiumDueAmount);
        if (paymentVo != null) {
          accessId = paymentVo.getAccessId();
          accessPass = paymentVo.getAccessPass();
          validGmo = true;
        }
      } catch (GmoPaymentException e) {
        validGmo = false;
        errMessage = e.getMessage();
      } catch (IllegalArgumentException | IllegalAccessException | IOException | ParseException
          | WebException e) {
        validGmo = false;
      } finally {
        // 入金詳細保存フラグ
        boolean billingDetailSaveFlag = true;

        DepositDetailsVo depositDetailsVo = new DepositDetailsVo();
        depositDetailsVo.setTenantId(tenantId); // テナントID
        depositDetailsVo.setEntryDate(batchDate); // 入力日 ＝ 入金の入金日
        depositDetailsVo.setBatchNo(batchNo); // バッチナンバー ＝ 入金のバッチナンバー
        depositDetailsVo.setCashDetailNo(++index); // 明細番号 ＝ 同一収納代行会社コード単位で、処理した順に１からの連番を採番
        depositDetailsVo.setContractNo(contractNo); // 契約番号 ＝ 請求詳細の証券番号
        depositDetailsVo.setContractBranchNo(contractBranchNo); // 証券番号枝番
        depositDetailsVo.setApplicationNo(null); // 申込番号 ＝ 属性初期値

        // 充当月 ＝ 請求詳細の充当月
        depositDetailsVo.setDueDate(dueDateForamt.parse(billingDetail.getDueDate()));
        // 合計保険料金額 ＝ 請求詳細の請求額
        depositDetailsVo.setTotalPremiumAmount(billingDetail.getTotalGrossPremium());
        // 入金金額
        // 1. カード決済APIレスポンスのエラーなしの場合、「合計保険料金額」と同値を設定
        // 2. カード決済APIレスポンスのエラーありの場合、0を設定
        depositDetailsVo.setDepositAmount(validGmo ? billingDetail.getTotalGrossPremium() : 0);
        depositDetailsVo.setCommissionWithheld(null); // 振込手数料 ＝ 属性初期値
        depositDetailsVo.setCompensationTax(null); // 振込手数料 消費税 ＝ 属性初期値
        depositDetailsVo.setClearingDate(null); // 消し込み日 ＝ 属性初期値
        depositDetailsVo.setSuspenceDate(batchDate); // サスペンス日 ＝ バッチ日付を設定
        depositDetailsVo.setDeleteDate(null); // 削除日 ＝ 属性初期値
        depositDetailsVo.setCashMatchingDate(null); // マッチング日 ＝ 属性初期値
        depositDetailsVo.setCashDetailStatus("S"); // 明細のステータス ＝ S（suspence）を設定
        // 引き去り結果コード カード
        if (validGmo) {
          depositDetailsVo.setPaymentResultCode("0"); // 決済OK（エラーなし）
        } else {
          // GMOエラー詳細コードチェック
          switch (errMessage) {
            case "42G120000":
            case "42G220000":
            case "42G300000":
            case "42G540000":
            case "42G560000":
            case "42G600000":
            case "42G610000":
            case "42G920000":
            case "42G950000":
            case "42G960000":
            case "42G970000":
            case "42G980000":
            case "42G990000":
              depositDetailsVo.setPaymentResultCode("1"); // カード無効
              break;
            case "42G030000":
            case "42G050000":
            case "42G070000":
            case "42G550000":
              depositDetailsVo.setPaymentResultCode("2"); // カード限度額オーバー
              break;
            case "42G020000":
            case "42G040000":
              depositDetailsVo.setPaymentResultCode("3"); // カード残高不足
              break;
            case "42G830000":
              depositDetailsVo.setPaymentResultCode("4"); // カードの有効期限範囲外
              break;
            default:
              billingDetailSaveFlag = false;
          }
        }
        depositDetailsVo.setAccessId(accessId); // 取引ID
        depositDetailsVo.setAccessPass(accessPass); // 取引パスワード
        depositDetailsVo.setComment(null); // 備考 ＝ 属性初期値
        depositDetailsVo.setPremiumDueDate(batchDate); // 保険料充当日 ＝ バッチ日付を設定
        // 保険料 連番 ＝ 請求詳細の保険料 連番
        depositDetailsVo.setPremiumSequenceNo(billingDetail.getPremiumSequenceNo());
        depositDetailsVo.setCreatedBy(createdBy); // 作成者 ＝ この処理の機能IDを設定
        // 保険料入金詳細データを作成する
        mapper.insertDepositDetails(depositDetailsVo);

        if (billingDetailSaveFlag) {
          batchTotalAmount += billingDetail.getTotalGrossPremium();
          receivedAmount += validGmo ? billingDetail.getTotalGrossPremium() : 0;
        }
      }
    }

    if (billingDetails != null && billingDetails.size() > 0) {
      // 保険料入金ヘッダを作成する。
      DepositHeadersVo depositHeadersVo = new DepositHeadersVo();
      depositHeadersVo.setTenantId(tenantId); // テナントID
      depositHeadersVo.setEntryDate(batchDate); // 入力日 ＝ バッチ日付を設定
      depositHeadersVo.setBatchNo(batchNo); // バッチナンバー ＝ 入金テーブルの入力日単位で1からの連番を設定
      depositHeadersVo.setPaymentMethodCode("3"); // 払込方法コード ＝ 3（カード）を設定
      depositHeadersVo.setDepositDate(batchDate); // 入金日 ＝ バッチ日付を設定
      // 入金金額 ＝ 作成した入金詳細の「合計保険料金額」を合算して設定
      depositHeadersVo.setReceivedAmount(String.valueOf(receivedAmount));
      // バッチ合計金額 ＝ 作成した入金詳細の「入金金額」を合算して設定
      depositHeadersVo.setBatchTotalAmount(String.valueOf(batchTotalAmount));
      depositHeadersVo.setBatchStatus("A"); // ステータス ＝ A（入力完了：マッチング待ち）を設定
      depositHeadersVo.setComment(null); // 備考 ＝ 属性初期値
      depositHeadersVo.setUsereId(createdBy); // 処理ユーザーID ＝ この処理の機能IDを設定
      depositHeadersVo.setCollectionRoute("R"); // 収集ルート ＝ R（レギュラー）を設定
      depositHeadersVo.setGroupCode(null); // 団体コード ＝ 属性初期値
      depositHeadersVo.setCreatedBy(createdBy); // 作成者 ＝ この処理の機能IDを設定

      // 保険料入金ヘッダの作成を実施
      mapper.insertDepositHeaders(depositHeadersVo);
    }
  }

  /**
   * 保険料請求情報に関わるテーブルを作成
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   */
  public void createPremiumHeaderData(ContractPremiumHeader contractPremiumHeader,
      String createdBy) {

    PremiumHeadersVo premiumHeader = convertToPremiumHeadersVo(contractPremiumHeader, createdBy);
    Map<String, Object> paramPremiumHeader = new HashMap<>();
    paramPremiumHeader.put("premiumHeader", premiumHeader);
    mapper.insertPremiumHeader(paramPremiumHeader);
  }

  /**
   * 保険料請求情報に関わるテーブルを作成
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   */
  public void createBillingData(ContractBillingVo contractBillingVo, String createdBy) {
    String billHeaderNo = "1";
    // 連番計算：請求テーブルの請求月、払込方法コード単位で1からの連番を設定
    if (contractBillingVo.getPaymentMethodCode() != null
        && contractBillingVo.getBatchDate() != null) {
      // パラムを準備する
      Map<String, Object> paramBillingHeaded = new HashMap<>();
      paramBillingHeaded.put("paymentMethodCode", contractBillingVo.getPaymentMethodCode());
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
      String billingPeriod = dateFormat.format(contractBillingVo.getBatchDate());
      paramBillingHeaded.put("billingPeriod", billingPeriod);
      // 請求テーブルの請求月
      billHeaderNo = mapper.getMaxBillingHeaderNo(paramBillingHeaded).get("billngHeaderNo");
    }
    // 請求（billing_headers）データを準備する
    BillingHeaderVo billingHeaderVo =
        createBillingHeader(contractBillingVo, billHeaderNo, createdBy);
    // 請求（billing_headers）に追加する
    mapper.insertBillingHeader(billingHeaderVo);
    BillingDetailVo billingDetailVo =
        createBillingDetail(contractBillingVo, billHeaderNo, createdBy);
    mapper.insertBillingDetails(billingDetailVo);
  }

  private BillingHeaderVo createBillingHeader(ContractBillingVo contractBilling,
      String billingHeaderNo, String createdBy) {
    BillingHeaderVo billingHeaderVo = new BillingHeaderVo();
    billingHeaderVo.setTenantId(contractBilling.getTenantId());

    if (contractBilling.getBatchDate() != null) {
      // バッチ日付値がある場合
      // 請求月がバッチ日付のYYYYMMを設定
      billingHeaderVo
          .setBillingPeriod(DateTimeUtils.convertDateToYearMonth(contractBilling.getBatchDate()));
      // 作成日がバッチ日付を設定
      billingHeaderVo.setCreateDate(contractBilling.getBatchDate());
    } else {
      // バッチ日付値がない場合
      // 請求月がnullを設定
      billingHeaderVo.setBillingPeriod(null);
      // 請求月がnullを設定
      billingHeaderVo.setCreateDate(null);
    }
    // 払込方法コード
    billingHeaderVo.setPaymentMethodCode(contractBilling.getPaymentMethodCode());
    // 連番
    billingHeaderVo.setPaymentMethodCode(billingHeaderNo);
    // 請求ヘッダー状態コード = B(Billed)を設定
    billingHeaderVo
        .setBillingHeaderStatus(Luigi2CodeBillingHeaders.BillingHeaderStatus.BILLED.toString());
    // 団体コード = 属性初期値
    billingHeaderVo.setGroupCode(null);
    // 収納代行会社コード = 収納代行会社コード
    billingHeaderVo.setFactoringCompanyCode(contractBilling.getFactoringCompanyCode());
    // 請求額 = 請求月、払込方法コード、連番に従属する請求詳細の保険料請求額premium_due_amountの合算値
    billingHeaderVo.setTotallBillerdAmount(contractBilling.getTotallBillerdAmount());
    // 入金額 = 0
    billingHeaderVo.setTotalReceivedAmount(0);
    // ロック用 = 1
    billingHeaderVo.setUpdateCount(1);
    // 作成者 = 請求作成 ID
    billingHeaderVo.setCreatedBy(createdBy);
    // 最後変更者 = 請求作成 ID
    billingHeaderVo.setUpdatedBy(createdBy);
    return billingHeaderVo;
  }

  private BillingDetailVo createBillingDetail(ContractBillingVo contractBilling,
      String billingHeaderNo, String createdBy) {
    BillingDetailVo billingDetailVo = new BillingDetailVo();
    billingDetailVo.setTenantId(contractBilling.getTenantId());

    // 請求月:バッチ日付がある場合： バッチ日付のYYYYMMを設定 ない場合：nullを設定
    billingDetailVo
        .setBillingPeriod(DateTimeUtils.convertDateToYearMonth(contractBilling.getBatchDate()));
    // 払込方法コード = 請求billing_headersの払込方法コードpayment_method_code
    billingDetailVo.setPaymentMethodCode(contractBilling.getPaymentMethodCode());
    // 連番 = 請求billing_headersの連番billng_header_no
    billingDetailVo.setBillngHeaderNo(contractBilling.getBillngHeaderNo());
    // 証券番号 = 保険料（premium_headers）.証券番号
    billingDetailVo.setContractNo(contractBilling.getContractNo());
    // 証券番号枝番 = 保険料（premium_headers）.証券番号枝番
    billingDetailVo.setContractBranchNo(contractBilling.getContractBranchNo());
    // 保険料充当日 = 属性初期値
    billingDetailVo.setPremiumDueDate(null);
    // 保険料 連番 = 保険料（premium_headers）.保険料連番
    billingDetailVo.setPremiumSequenceNo(contractBilling.getPremiumSequenceNo());
    // 充当月 = 保険料（premium_headers).保険料収納月
    if (contractBilling.getDueDate() != null) {
      billingDetailVo.setDueDate(contractBilling.getDueDate());
    }
    // 保険料請求額 = 保険料（premium_headers）.グロス保険料
    billingDetailVo.setPremiumDueAmount(contractBilling.getPremiumDueAmount());
    // 銀行コード = 契約者払込方法= （policy_holders_pay_method）.銀行コード
    billingDetailVo.setBankCode(contractBilling.getBankCode());
    // 支店コード = 契約者払込方法= （policy_holders_pay_method）.支店コード
    billingDetailVo.setBankBranchCode(contractBilling.getBankBranchCode());
    // 口座種別 = 契約者払込方法= （policy_holders_pay_method）.口座種別
    billingDetailVo.setBankAccountType(contractBilling.getBankAccountType());
    // 口座番号 = 契約者払込方法= （policy_holders_pay_method）.口座番号
    billingDetailVo.setBankAccountNo(contractBilling.getBankAccountNo());
    // カード番号(トークン) = 契約者払込方法= （policy_holders_pay_method）.カード番号(トークン)
    billingDetailVo.setTokenNo(contractBilling.getTokenNo());
    // 引き去り結果コード 銀行 = SPACEを設定
    billingDetailVo.setBankResultCode(" ");
    // 引き去り結果コード カード = SPACEを設定
    billingDetailVo.setCardResultCode(" ");
    // 引き去り結果コード その他 = SPACEを設定
    billingDetailVo.setOtherResultCode(" ");
    // 氏名（漢字）の姓/会社 = 属性初期値
    billingDetailVo.setNameKnjSei(null);
    // 氏名（漢字）の名 = 属性初期値
    billingDetailVo.setNameKnjMei(null);
    // 氏名（カナ）の姓/会社 = 属性初期値
    billingDetailVo.setNameKanaSei(null);
    // 氏名（カナ）の名 = 属性初期値
    billingDetailVo.setNameKanaMei(null);
    // 作成者 = 請求作成 ID
    billingDetailVo.setCreatedBy(createdBy);
    return billingDetailVo;
  }

  private PremiumHeadersVo convertToPremiumHeadersVo(ContractPremiumHeader contractPremiumHeader,
      String createdBy) {
    PremiumHeadersVo premiumHeader = new PremiumHeadersVo();
    // tenantId
    premiumHeader.setTenantId(contractPremiumHeader.getTenantId());
    // 証券番号 = 契約（contracts）.証券番号
    premiumHeader.setContractNo(contractPremiumHeader.getContractNo());
    // 証券番号枝番 = 契約（contracts）.証券番号枝番
    premiumHeader.setContractBranchNo(contractPremiumHeader.getContractBranchNo());
    // 初回保険料フラグ(first_premium) = 属性初期値
    premiumHeader.setFirstPremium("0");
    // 異動日（effective_date）＝ 属性初期値
    premiumHeader.setEffectiveDate(null);
    // グロス保険料 total_gross_premium = 契約（contracts）. 合計保険料(total_premium)
    premiumHeader.setTotalGrossPremium(contractPremiumHeader.getTotalPremium());

    // ステータス（premium_status）＝ 固定値：P（Pending）を設定
    premiumHeader.setPremiumStatus("P");

    // 同一証券番号のmax(保険料.保険料連番)+1
    Map<String, Object> paramMaxPremiumSequenNo = new HashMap<>();
    paramMaxPremiumSequenNo.put("contractNo", contractPremiumHeader.getContractNo());

    PremiumHeadersVo maxPremiumSequenNo = mapper.getMaxPremiumSequenceNo(paramMaxPremiumSequenNo);
    // 保険料連番（premium_headers）.premium_sequence_no 保険料 = 保険料連番 premium_sequence_no
    // 同一証券番号のmax(保険料.保険料連番)+1
    premiumHeader.setPremiumSequenceNo(maxPremiumSequenNo.getPremiumSequenceNo());

    // 保険料収納月(premium_billing_period) =
    // 同一証券番号のmax(保険料.保険料連番)の行から取得した（保険料収納月+12/保険料払込回数）の年月
    // 1件目は契約日と同じ。また払込回数=00はBD-008の対象外。
    Integer frequency = Integer.parseInt(contractPremiumHeader.getFrequency());
    if (maxPremiumSequenNo.getPremiumSequenceNo() > 1 && frequency != 0) {
      String premiumBillingPeriod = DateTimeUtils.addMonthToYearMonth(
          maxPremiumSequenNo.getPremiumBillingPeriod(), (Integer) 12 / frequency);
      premiumHeader.setPremiumBillingPeriod(premiumBillingPeriod);

      // 保険料充当日:下で決定した保険料収納月premium_billing_period: 契約日contracts.issue_dateの日(dd)
      String premiumDueDate = DateTimeUtils.convertDateToDay(contractPremiumHeader.getIssueDate());

      premiumHeader.setPremiumDueDate(
          DateTimeUtils.addDayToYearMonth(premiumBillingPeriod, Integer.parseInt(premiumDueDate)));
    }

    // 保険料払込回数 = 保険料払込回数.frequency
    premiumHeader.setFrequency(null);
    // 返金日（refunded_date）＝ 属性初期値
    premiumHeader.setRefundeDate(null);
    // 取消日（canceled_date）＝ 属性初期値

    // 作成者 ＝ この処理の機能IDを設定
    premiumHeader.setCreatedBy(createdBy);
    return premiumHeader;
  }
}