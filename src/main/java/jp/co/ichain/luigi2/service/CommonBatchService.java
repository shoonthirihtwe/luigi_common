package jp.co.ichain.luigi2.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.batch.BatchService;
import jp.co.ichain.luigi2.mapper.CommonBatchMapper;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeBillingHeaders;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeBillingHeaders.BillingHeaderStatus;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeDepositDetails.CashDetailStatus;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeDepositDetails.PaymentResultCode;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeDepositHeaders;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeDepositHeaders.BatchStatus;
import jp.co.ichain.luigi2.resources.code.Luigi2CodeDepositHeaders.CollectionRoute;
import jp.co.ichain.luigi2.resources.code.Luigi2CodePremium.PremiumStatus;
import jp.co.ichain.luigi2.service.pay.PaymentService;
import jp.co.ichain.luigi2.service.pay.gmo.GmoPaymentProperties;
import jp.co.ichain.luigi2.util.CollectionUtils;
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
import jp.co.ichain.luigi2.vo.TenantsVo;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Batchサービス
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-08-12
 * @updatedAt : 2021-08-12
 */
@Slf4j
@Service
public class CommonBatchService {

  public static boolean TEST_FLAG = false;

  @Autowired
  CommonBatchMapper mapper;

  @Autowired
  PaymentService paymentService;

  private SimpleDateFormat batchForamt = new SimpleDateFormat("yyyyMMdd");

  /**
   * バッチを実行する
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-08-18
   * @updatedAt : 2021-08-18
   * @param batchMap
   * @param args
   * @throws ParseException
   */
  public void runBatch(Map<String, BatchService> batchMap, String... args) throws ParseException {

    if (args.length == 0) {
      return;
    }

    // テナントID & バッチ日付を取得
    List<TenantsVo> tenantList = new ArrayList<TenantsVo>();
    for (val tmp : CollectionUtils.safe(args[0].split(","))) {
      val tenant = new TenantsVo();
      val tmp2 = tmp.split(":");
      tenant.setId(Integer.valueOf(tmp2[0]));
      tenant.setBatchDate(batchForamt.parse(tmp2[1]));
      tenantList.add(tenant);
    }

    Set<String> keys = null;
    if (args.length > 1) {
      keys = new HashSet<String>();
      keys.add(args[1]);
    } else {
      keys = batchMap.keySet();
    }

    keys.forEach(key -> {
      log.info(key + "開始");
      try {
        batchMap.get(key).run(tenantList);
      } catch (Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        log.error(sw.toString());
      }
      log.info(key + "完了");
    });

    if (TEST_FLAG == false) {
      System.exit(0);
    }
  }

  /**
   * 請求決済処理を実施し入金データを作成
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-06
   * @updatedAt : 2022-04-06
   * @param billingDetails
   * @param tenantId
   * @param batchDate
   * @param createdBy
   * @throws ParseException
   */
  public void payAndCreateDepositData(List<BillingDetailsVo> billingDetails, int tenantId,
      Date batchDate, String createdBy) throws ParseException {
    payAndCreateDepositData(billingDetails, tenantId, batchDate,
        Luigi2CodeDepositHeaders.PaymentMethodCode.CARD, createdBy);
  }

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
      Date batchDate, Luigi2CodeDepositHeaders.PaymentMethodCode paymentMethodCode,
      String createdBy) throws ParseException {

    // バッチナンバー
    int batchNo = mapper.selectBatchNo(batchDate);

    // GMOのバリデーションフラグ (一回でも成功したら）
    boolean validGmoOneSuccess = false;

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
      Integer premiumDueAmount = billingDetail.getPremiumDueAmount(); // 合計保険料金額

      // GMO決済サービス
      PaymentVo paymentVo = null;
      try {
        paymentVo = paymentService.pay(billingDetail, batchDate);
        if (paymentVo != null) {
          validGmo = true;
          validGmoOneSuccess = true;
        }
      } catch (Exception e) {
        validGmo = false;
        log.error(e.getMessage());
      } finally {
        billingDetail.setGmApiSuccess(validGmo);
        DepositDetailsVo depositDetailsVo = new DepositDetailsVo();
        depositDetailsVo.setTenantId(tenantId); // テナントID
        depositDetailsVo.setEntryDate(batchDate); // 入力日 ＝ 入金の入金日
        depositDetailsVo.setBatchNo(batchNo); // バッチナンバー ＝ 入金のバッチナンバー
        depositDetailsVo.setCashDetailNo(++index); // 明細番号 ＝ 同一収納代行会社コード単位で、処理した順に１からの連番を採番
        depositDetailsVo.setContractNo(contractNo); // 契約番号 ＝ 請求詳細の証券番号
        depositDetailsVo.setContractBranchNo(contractBranchNo); // 証券番号枝番
        depositDetailsVo.setApplicationNo(null); // 申込番号 ＝ 属性初期値

        // 充当月 ＝ 請求詳細の充当月
        depositDetailsVo.setDueDate(billingDetail.getDueDate());
        // 合計保険料金額 ＝ 請求詳細の請求額
        depositDetailsVo.setTotalPremiumAmount(premiumDueAmount);
        // 入金金額
        // 1. カード決済APIレスポンスのエラーなしの場合、「合計保険料金額」と同値を設定
        // 2. カード決済APIレスポンスのエラーありの場合、0を設定
        depositDetailsVo.setDepositAmount(validGmo ? premiumDueAmount : 0);
        depositDetailsVo.setCommissionWithheld(null); // 振込手数料 ＝ 属性初期値
        depositDetailsVo.setCompensationTax(null); // 振込手数料 消費税 ＝ 属性初期値
        depositDetailsVo.setClearingDate(null); // 消し込み日 ＝ 属性初期値
        depositDetailsVo.setSuspenceDate(batchDate); // サスペンス日 ＝ バッチ日付を設定
        depositDetailsVo.setDeleteDate(null); // 削除日 ＝ 属性初期値
        depositDetailsVo.setCashMatchingDate(null); // マッチング日 ＝ 属性初期値
        // 明細のステータス ＝ S（suspence）を設定
        depositDetailsVo.setCashDetailStatus(CashDetailStatus.SUSPENCE.toString());
        // 引き去り結果コード カード
        if (validGmo) {
          switch (paymentMethodCode) {
            case BANK:
              // ステータス ＝ N（原因不明）を設定
              depositDetailsVo.setPaymentResultCode(PaymentResultCode.UNKNOWN.toString());
              break;
            case CARD:
              // 決済OK（エラーなし）
              depositDetailsVo.setPaymentResultCode(PaymentResultCode.SUCCESS.toString()); //
              break;
            default:
              depositDetailsVo.setPaymentResultCode(PaymentResultCode.UNKNOWN.toString()); // 決済OK（エラーなし）
              break;
          }

        } else {
          // GMOエラー詳細コードチェック
          val errList = paymentVo.getErrorList();
          String errorInfo = "";
          if (errList != null && errList.size() > 0) {
            errorInfo = errList.get(0).getErrInfo();
          }
          val paymentResultCode =
              GmoPaymentProperties.getInstance().errorPaymentResultMap.get(errorInfo);
          if (paymentResultCode != null) {
            depositDetailsVo.setPaymentResultCode(paymentResultCode);
          } else {
            depositDetailsVo.setPaymentResultCode(PaymentResultCode.UNKNOWN.toString());
          }
        }
        depositDetailsVo.setFactoringTransactionId(paymentVo.getFactoringTransactionId());
        depositDetailsVo.setAccessId(paymentVo.getAccessId()); // 取引ID
        depositDetailsVo.setAccessPass(paymentVo.getAccessPass()); // 取引パスワード
        depositDetailsVo.setComment(null); // 備考 ＝ 属性初期値
        depositDetailsVo.setPremiumDueDate(billingDetail.getPremiumDueDate()); // 保険料充当日 ＝ バッチ日付を設定
        // 保険料 連番 ＝ 請求詳細の保険料 連番
        depositDetailsVo.setPremiumSequenceNo(billingDetail.getPremiumSequenceNo());
        depositDetailsVo.setCreatedBy(createdBy); // 作成者 ＝ この処理の機能IDを設定
        // 保険料入金詳細データを作成する
        mapper.insertDepositDetails(depositDetailsVo);

        // 請求テーブルに取引用IDセット
        mapper.updateBillingDetail(depositDetailsVo);

        // 作成した入金詳細の「合計保険料金額」を合算して設定
        receivedAmount += premiumDueAmount;
        // 作成した入金詳細の「入金金額」を合算して設定
        batchTotalAmount += validGmo ? premiumDueAmount : 0;
      }

      BillingHeaderVo billingHeaderVo = new BillingHeaderVo();
      billingHeaderVo.setBillingHeaderStatus(BillingHeaderStatus.BILLED.toString());
      billingHeaderVo.setUpdatedBy(createdBy);
      billingHeaderVo.setId(String.valueOf(billingDetail.getBillingHeaderId()));

      mapper.updateBillingHeader(billingHeaderVo);
    }

    if (billingDetails != null && billingDetails.size() > 0) {
      // 保険料入金ヘッダを作成する。
      DepositHeadersVo depositHeadersVo = new DepositHeadersVo();
      depositHeadersVo.setTenantId(tenantId); // テナントID
      depositHeadersVo.setEntryDate(batchDate); // 入力日 ＝ バッチ日付を設定
      depositHeadersVo.setBatchNo(batchNo); // バッチナンバー ＝ 入金テーブルの入力日単位で1からの連番を設定
      // 払込方法コード
      depositHeadersVo.setPaymentMethodCode(paymentMethodCode.toString());
      depositHeadersVo.setDepositDate(batchDate); // 入金日 ＝ バッチ日付を設定
      // 入金金額 ＝ 作成した入金詳細の「合計保険料金額」を合算して設定
      depositHeadersVo.setReceivedAmount(String.valueOf(receivedAmount));
      // バッチ合計金額 ＝ 作成した入金詳細の「入金金額」を合算して設定
      depositHeadersVo.setBatchTotalAmount(String.valueOf(batchTotalAmount));

      depositHeadersVo.setComment(null); // 備考 ＝ 属性初期値
      depositHeadersVo.setUsereId(createdBy); // 処理ユーザーID ＝ この処理の機能IDを設定
      // 収集ルート ＝ R（レギュラー）を設定
      depositHeadersVo.setCollectionRoute(CollectionRoute.REGULAR.toString());
      depositHeadersVo.setGroupCode(null); // 団体コード ＝ 属性初期値
      depositHeadersVo.setCreatedBy(createdBy); // 作成者 ＝ この処理の機能IDを設定

      switch (paymentMethodCode) {
        case BANK:
          if (validGmoOneSuccess) {
            // ステータス ＝ T（口座振替待ち）を設定
            depositHeadersVo.setBatchStatus(BatchStatus.TRANSFER.toString());
          } else {
            // ステータス ＝ A（入力完了：マッチング待ち）を設定
            depositHeadersVo.setBatchStatus(BatchStatus.WAITING.toString());
          }
          break;
        case CARD:
        default:
          // ステータス ＝ A（入力完了：マッチング待ち）を設定
          depositHeadersVo.setBatchStatus(BatchStatus.WAITING.toString());
          break;
      }
      // 保険料入金ヘッダの作成を実施
      mapper.insertDepositHeaders(depositHeadersVo);
    }

  }

  /**
   * 保険料請求情報に関わるテーブルを作成
   * 
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   */
  public void createPremiumHeaderData(ContractPremiumHeader contractPremiumHeader,
      boolean isFirstPremium, String createdBy) {
    // データ準備
    PremiumHeadersVo premiumHeader =
        convertToPremiumHeadersVo(contractPremiumHeader, isFirstPremium, createdBy);

    Map<String, Object> paramPremiumHeader = new HashMap<>();
    paramPremiumHeader.put("premiumHeader", premiumHeader);
    // データ追加
    mapper.insertPremiumHeader(paramPremiumHeader);
  }

  /**
   * 請求詳細（billing_detail）データ追加
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   */
  public void createBillingData(ContractBillingVo contractBillingVo, String createdBy) {
    int billHeaderNo = 1;
    // 連番計算：請求テーブルの請求月、払込方法コード単位で1からの連番を設定
    if (contractBillingVo.getPaymentMethodCode() != null
        && contractBillingVo.getBatchDate() != null) {
      // パラムを準備する
      Map<String, Object> paramBillingHeaded = new HashMap<>();
      paramBillingHeaded.put("paymentMethodCode", contractBillingVo.getPaymentMethodCode());
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
      String billingPeriod = dateFormat.format(contractBillingVo.getBatchDate());
      paramBillingHeaded.put("billingPeriod", billingPeriod);
      paramBillingHeaded.put("tenantId", contractBillingVo.getTenantId());
      // 請求テーブルの請求月
      billHeaderNo = mapper.selectMaxBillingHeaderNo(paramBillingHeaded);
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

  /**
   * 請求（billing_headers）データ追加
   * 
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   */
  public void createBillingHeaders(List<ContractBillingVo> contractBillingVos, String createdBy) {
    for (ContractBillingVo contractBillingVo : contractBillingVos) {
      int billHeaderNo = 1;
      // 連番計算：請求テーブルの請求月、払込方法コード単位で1からの連番を設定
      if (contractBillingVo.getPaymentMethodCode() != null
          && contractBillingVo.getBatchDate() != null) {
        // パラムを準備する
        Map<String, Object> paramBillingHeaded = new HashMap<>();
        paramBillingHeaded.put("paymentMethodCode", contractBillingVo.getPaymentMethodCode());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String billingPeriod = dateFormat.format(contractBillingVo.getBatchDate());
        paramBillingHeaded.put("billingPeriod", billingPeriod);
        paramBillingHeaded.put("tenantId", contractBillingVo.getTenantId());
        // 請求テーブルの請求月
        billHeaderNo = mapper.selectMaxBillingHeaderNo(paramBillingHeaded);
      }
      // 請求（billing_headers）データを準備する
      BillingHeaderVo billingHeaderVo =
          createBillingHeader(contractBillingVo, billHeaderNo, createdBy);
      try {
        mapper.insertBillingHeader(billingHeaderVo);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  /**
   * 請求詳細（billing_detail）データ追加
   *
   * @author : [VJP] HOANGNH
   * @createdAt : 2021-08-12
   * @updatedAt : 2021-08-12
   * @return
   */
  public void createBillingDetails(List<ContractBillingVo> contractBillingVos, String createdBy) {
    for (ContractBillingVo contractBillingVo : contractBillingVos) {
      Integer billHeaderNo = 1;
      // 連番計算：請求テーブルの請求月、払込方法コード単位で1からの連番を設定
      if (contractBillingVo.getPaymentMethodCode() != null
          && contractBillingVo.getBatchDate() != null) {
        // パラムを準備する
        Map<String, Object> paramBillingHeaded = new HashMap<>();
        paramBillingHeaded.put("paymentMethodCode", contractBillingVo.getPaymentMethodCode());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String billingPeriod = dateFormat.format(contractBillingVo.getBatchDate());
        paramBillingHeaded.put("billingPeriod", billingPeriod);
        paramBillingHeaded.put("tenantId", contractBillingVo.getTenantId());
        paramBillingHeaded.put("factoringCompanyCode", contractBillingVo.getFactoringCompanyCode());
        // 請求テーブルの請求月
        billHeaderNo = mapper.selectBillingHeaderNo(paramBillingHeaded);
      }
      BillingDetailVo billingDetailVo =
          createBillingDetail(contractBillingVo, billHeaderNo, createdBy);
      try {
        mapper.insertBillingDetails(billingDetailVo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private BillingHeaderVo createBillingHeader(ContractBillingVo contractBilling,
      Integer billingHeaderNo, String createdBy) {
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
    billingHeaderVo.setBillngHeaderNo(billingHeaderNo);
    // 請求ヘッダー状態コード = B(Billed)を設定
    billingHeaderVo.setBillingHeaderStatus(
        Luigi2CodeBillingHeaders.BillingHeaderStatus.DATA_CREATED.toString());
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
      Integer billingHeadNo, String createdBy) {
    BillingDetailVo billingDetailVo = new BillingDetailVo();
    billingDetailVo.setTenantId(contractBilling.getTenantId());

    // 請求月:バッチ日付がある場合： バッチ日付のYYYYMMを設定 ない場合：nullを設定
    billingDetailVo
        .setBillingPeriod(DateTimeUtils.convertDateToYearMonth(contractBilling.getBatchDate()));
    // 払込方法コード = 請求billing_headersの払込方法コードpayment_method_code
    billingDetailVo.setPaymentMethodCode(contractBilling.getPaymentMethodCode());
    // 連番 = 請求billing_headersの連番billng_header_no
    billingDetailVo.setBillngHeaderNo(billingHeadNo);
    // 証券番号 = 保険料（premium_headers）.証券番号
    billingDetailVo.setContractNo(contractBilling.getContractNo());
    // 証券番号枝番 = 保険料（premium_headers）.証券番号枝番
    billingDetailVo.setContractBranchNo(contractBilling.getContractBranchNo());
    // 保険料充当日 = 属性初期値
    billingDetailVo.setPremiumDueDate(contractBilling.getPremiumDueDate());
    // 保険料 連番 = 保険料（premium_headers）.保険料連番
    billingDetailVo.setPremiumSequenceNo(contractBilling.getPremiumSequenceNo());
    // 充当月 = 保険料（premium_headers).保険料収納月
    if (contractBilling.getDueDate() != null) {
      billingDetailVo.setDueDate(contractBilling.getDueDate());
    }
    // 保険料請求額 = 保険料（premium_headers）.グロス保険料
    billingDetailVo.setPremiumDueAmount(contractBilling.getTotallBillerdAmount());
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
      boolean isFirstPremium, String createdBy) {
    PremiumHeadersVo premiumHeader = new PremiumHeadersVo();
    // tenantId
    premiumHeader.setTenantId(contractPremiumHeader.getTenantId());
    // 証券番号 = 契約（contracts）.証券番号
    premiumHeader.setContractNo(contractPremiumHeader.getContractNo());
    // 証券番号枝番 = 契約（contracts）.証券番号枝番
    premiumHeader.setContractBranchNo(contractPremiumHeader.getContractBranchNo());

    if (isFirstPremium) {
      premiumHeader.setFirstPremium("1");
      premiumHeader.setEffectiveDate(contractPremiumHeader.getBatchDate());
    } else {
      // 初回保険料フラグ(first_premium) = 属性初期値
      premiumHeader.setFirstPremium("0");
      // 異動日（effective_date）＝ 属性初期値
      premiumHeader.setEffectiveDate(null);
    }

    // グロス保険料 total_gross_premium = 契約（contracts）. 合計保険料(total_premium)
    premiumHeader.setTotalGrossPremium(contractPremiumHeader.getTotalPremium());

    // ステータス（premium_status）＝ 固定値：P（Pending）を設定
    premiumHeader.setPremiumStatus(PremiumStatus.PENDING.toString());

    // 同一証券番号のmax(保険料.保険料連番)+1
    Map<String, Object> paramMaxPremiumSequenNo = new HashMap<>();
    paramMaxPremiumSequenNo.put("contractNo", contractPremiumHeader.getContractNo());
    paramMaxPremiumSequenNo.put("tenantId", contractPremiumHeader.getTenantId());
    paramMaxPremiumSequenNo.put("contractBranchNo", contractPremiumHeader.getContractBranchNo());

    PremiumHeadersVo maxPremiumSequenNo = mapper.getMaxPremiumSequenceNo(paramMaxPremiumSequenNo);
    // 保険料連番（premium_headers）.premium_sequence_no 保険料 = 保険料連番 premium_sequence_no
    // 同一証券番号のmax(保険料.保険料連番)+1
    if (maxPremiumSequenNo != null) {
      premiumHeader.setPremiumSequenceNo(maxPremiumSequenNo.getPremiumSequenceNo());
    } else {
      premiumHeader.setPremiumSequenceNo(1);
    }
    // 保険料収納月(premium_billing_period) =
    // 同一証券番号のmax(保険料.保険料連番)の行から取得した（保険料収納月+12/保険料払込回数）の年月
    // 1件目は契約日と同じ。また払込回数=00はBD-008の対象外。
    Integer frequency = Integer.parseInt(contractPremiumHeader.getFrequency());

    if (maxPremiumSequenNo != null && maxPremiumSequenNo.getPremiumSequenceNo() > 1
        && frequency != 0) {
      String premiumBillingPeriod = DateTimeUtils
          .addMonthToYearMonth(maxPremiumSequenNo.getPremiumBillingPeriod(), 12 / frequency);
      premiumHeader.setPremiumBillingPeriod(premiumBillingPeriod);

      // 保険料充当日:下で決定した保険料収納月premium_billing_period: 契約日contracts.issue_dateの日(dd)
      String premiumDueDate = DateTimeUtils.convertDateToDay(contractPremiumHeader.getIssueDate());

      premiumHeader.setPremiumDueDate(
          DateTimeUtils.addDayToYearMonth(premiumBillingPeriod, Integer.parseInt(premiumDueDate)));
    } else {
      premiumHeader.setPremiumBillingPeriod(
          DateTimeUtils.convertDateToYearMonth(contractPremiumHeader.getIssueDate()));
      premiumHeader.setPremiumDueDate(contractPremiumHeader.getIssueDate());
    }

    // frequency = 契約（contracts）の保険料払込回数(frequency)
    premiumHeader.setFrequency(Integer.valueOf(contractPremiumHeader.getFrequency()));
    // 返金日（refunded_date）＝ 属性初期値
    premiumHeader.setRefundeDate(null);
    // 取消日（canceled_date）＝ 属性初期値

    // 作成者 ＝ この処理の機能IDを設定
    premiumHeader.setCreatedBy(createdBy);
    return premiumHeader;
  }
}
