package jp.co.ichain.luigi2.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.exception.WebException;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import jp.co.ichain.luigi2.resources.ServiceInstancesResources;
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
  ValidityResources validityResources;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

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
   */
  public void validate(Map<String, Object> paramMap, String endpoint)
      throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {

    val tenantId = (Integer) paramMap.get("tenantId");
    val validityMap = validityResources.get(tenantId);
    Optional<ServiceInstancesVo> optional =
        serviceInstancesResources.getFirst(tenantId, Validity.getValiditySourceKey(endpoint));

    // sourceKeyが存在しない場合
    if (optional.isEmpty()) {
      throw new WebDataException(Luigi2Code.D0002, "sourceKey",
          Validity.getValiditySourceKey(endpoint));
    }
    val serviceInstanceMap = optional.get().getInherentMap();

    // validate
    val exList = new ArrayList<WebException>();
    Validity.validate(validityMap, serviceInstanceMap, paramMap, exList);
  }
}