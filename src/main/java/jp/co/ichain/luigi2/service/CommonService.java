package jp.co.ichain.luigi2.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.exception.WebParameterException;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.resources.ValidityResources;
import jp.co.ichain.luigi2.validity.Validity;
import jp.co.ichain.luigi2.vo.ServiceInstancesVo;
import lombok.val;

/**
 * 認証サービス
 * 
 * @author : [AOT] s.park
 * @createdAt : 2021-03-05
 * @updatedAt : 2021-03-05
 */
@Service
public class CommonService {

  @Autowired
  Validity validity;

  @Autowired
  ValidityResources validityResources;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

  /**
   * 検証を行う
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-08
   * @updatedAt : 2021-06-08
   * @param paramMap
   * @param endpoint
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws UnsupportedEncodingException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @Transactional(transactionManager = "luigi2TransactionManager", readOnly = true,
      propagation = Propagation.NESTED)
  public void validate(Map<String, Object> paramMap, String endpoint)
      throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    val tenantId = (Integer) paramMap.get("tenantId");
    val validityMap = validityResources.get(tenantId);
    Optional<ServiceInstancesVo> optional =
        serviceInstancesBaseResources.getFirst(tenantId, validity.getValiditySourceKey(endpoint));

    // sourceKeyが存在しない場合
    if (optional == null || optional.isEmpty()) {
      throw new WebDataException(Luigi2ErrorCode.D0002, "sourceKey",
          validity.getValiditySourceKey(endpoint));
    }
    val serviceInstanceMap = optional.get().getInherentMap();

    // validate
    val exList = new ArrayList<WebException>();

    if (serviceInstanceMap.get("param-key") != null) {
      // Condition
      validity.validateCondition(validityMap.get(serviceInstanceMap.get("param-key")), null, null,
          "this", paramMap, tenantId, exList);
    }
    validity.validate(validityMap, serviceInstanceMap, tenantId, null, null, paramMap, exList);

    if (exList.size() > 0) {
      throw new WebParameterException(Luigi2ErrorCode.V0000, exList);
    }
  }
}
