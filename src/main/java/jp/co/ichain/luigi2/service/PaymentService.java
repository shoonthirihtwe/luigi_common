package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.vo.FactoringCompaniesVo;
import jp.co.ichain.luigi2.vo.PaymentVo;

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

}
