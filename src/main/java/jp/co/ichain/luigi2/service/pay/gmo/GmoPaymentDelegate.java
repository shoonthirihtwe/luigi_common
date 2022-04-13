package jp.co.ichain.luigi2.service.pay.gmo;

import java.io.IOException;
import java.text.ParseException;
import org.springframework.stereotype.Service;
import jp.co.ichain.luigi2.exception.GmoPaymentException;
import jp.co.ichain.luigi2.vo.FactoringCompaniesVo;
import jp.co.ichain.luigi2.vo.GmoPaymentVo;

/**
 * Gmo Service
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-22
 * @updatedAt : 2021-06-22
 */
@Service
public interface GmoPaymentDelegate {
  /**
   * 決済実行
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
  GmoPaymentVo execTran(GmoPaymentVo gmoPaymentVo, FactoringCompaniesVo companiesVo)
      throws GmoPaymentException, IllegalArgumentException, IllegalAccessException, IOException,
      ParseException;

  /**
   * 決済取り消し実行
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-06
   * @updatedAt : 2022-04-06
   * @param gmoPaymentVo
   * @param appendData
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws GmoPaymentException
   * @throws IOException
   * @throws ParseException
   */
  GmoPaymentVo cancel(GmoPaymentVo gmoPaymentVo, Object appendData) throws IllegalArgumentException,
      IllegalAccessException, GmoPaymentException, IOException, ParseException;
}
