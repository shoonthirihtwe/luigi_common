package jp.co.ichain.luigi2.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.dao.AwsTransferS3Dao;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.util.CollectionUtils;
import jp.co.ichain.luigi2.vo.FactoringCompaniesVo;
import jp.co.ichain.luigi2.vo.PaymentVo;
import lombok.val;

/**
 * 決済サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-28
 * @updatedAt : 2021-06-28
 */
@Service
public class PaymentService {

  @Autowired
  GmoPaymentService gmoPaymentService;

  @Autowired
  CommonMapper commonMapper;

  @Autowired
  AwsTransferS3Dao awsTransferS3Dao;

  /**
   * 決済実行
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-28
   * @updatedAt : 2021-06-28
   * @param contractNo
   * @param cardCustNumber
   * @param dueDate
   * @param premiumDueAmount
   * @param nowDate(onlineDate, バッチの場合 batchDate)
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  public PaymentVo pay(Integer tenantId, String contractNo, String cardCustNumber, String dueDate,
      Integer premiumDueAmount, Date nowDate) throws IllegalArgumentException,
      IllegalAccessException, GmoPaymentException, IOException, ParseException {

    FactoringCompaniesVo companyInfo =
        commonMapper.selectFactoringCompanyCode(tenantId, contractNo, nowDate);

    if (companyInfo == null) {
      throw new WebDataException(Luigi2ErrorCode.D0002, "contractNo");
    }

    PaymentVo result = null;
    switch (companyInfo.getFactoringCompanyCode()) {
      case "CARD01":
        result = gmoPaymentService.pay(companyInfo, contractNo, cardCustNumber, dueDate,
            premiumDueAmount);
        break;
      default:
        throw new WebDataException(Luigi2ErrorCode.D0001, "factoringCompanyCode");
    }

    return result;
  }

  /**
   * 決済取り消し
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-28
   * @updatedAt : 2021-06-28
   * @param contractNo
   * @param accessId
   * @param accessPassword
   * @param suspenceDate
   * @param nowDate (onlineDate, バッチの場合 batchDate)
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  public PaymentVo cancel(Integer tenantId, String contractNo, String accessId,
      String accessPassword, Date suspenceDate, Date nowDate) throws IllegalArgumentException,
      IllegalAccessException, GmoPaymentException, IOException, ParseException {

    FactoringCompaniesVo companyInfo =
        commonMapper.selectFactoringCompanyCode(tenantId, contractNo, nowDate);

    if (companyInfo == null) {
      throw new WebDataException(Luigi2ErrorCode.D0001);
    }

    PaymentVo result = null;
    switch (companyInfo.getFactoringCompanyCode()) {
      case "CARD01":
        result = gmoPaymentService.cancel(companyInfo, accessId, accessPassword, suspenceDate);
        break;
      default:
        throw new WebDataException(Luigi2ErrorCode.D0001, "factoringCompanyCode");
    }

    return result;
  }

  /**
   * 口座振替CSV作成してアップロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-01
   * @updatedAt : 2022-04-01
   * @param tenantId
   * @param contractNo
   * @param dataList
   * @param nowDate
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  public void uploadAccountTransferCsv(Integer tenantId,
      List<Map<String, Object>> dataList, Date nowDate) throws IllegalArgumentException,
      IllegalAccessException, GmoPaymentException, IOException, ParseException {

    val factoringCompanyMap = new HashMap<String, FactoringCompaniesVo>();
    val factoringDataMap = new HashMap<String, List<Map<String, Object>>>();
    for (val data : dataList) {
      try {
        FactoringCompaniesVo companyInfo = commonMapper.selectFactoringCompanyCode(tenantId,
            (String) data.get("contractNo"), nowDate);

        if (companyInfo == null) {
          throw new WebDataException(Luigi2ErrorCode.D0002, "contractNo");
        }

        var list = factoringDataMap.get(companyInfo.getFactoringCompanyCode());
        if (list == null) {
          list = new ArrayList<Map<String, Object>>();
          factoringDataMap.put(companyInfo.getFactoringCompanyCode(), list);
          factoringCompanyMap.put(companyInfo.getFactoringCompanyCode(), companyInfo);
        }
        list.add(data);
      } catch (WebDataException e) {
        e.printStackTrace();
      }
    }

    for (val companyInfo : CollectionUtils.safe(factoringCompanyMap.values())) {
      try {
        switch (companyInfo.getFactoringCompanyCode()) {
          // TODO 口座振替コード
          case "CARD01":
            val result = gmoPaymentService.makeAccountTransferCsv(companyInfo,
                factoringDataMap.get(companyInfo.getFactoringCompanyCode()));

            // s3 upload
            // TODO Bucket path setting
            awsTransferS3Dao.upload(companyInfo.getBillingMonth(),
                new ByteArrayInputStream(result.toByteArray()));
            break;
          default:
            throw new WebDataException(Luigi2ErrorCode.D0001, "factoringCompanyCode");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
