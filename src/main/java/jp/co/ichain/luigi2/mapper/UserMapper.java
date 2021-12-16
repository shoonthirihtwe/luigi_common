package jp.co.ichain.luigi2.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import jp.co.ichain.luigi2.config.datasource.Luigi2Mapper;
import jp.co.ichain.luigi2.vo.AuthoritiesVo;
import jp.co.ichain.luigi2.vo.NayoseResultVo;
import jp.co.ichain.luigi2.vo.TenantsVo;
import jp.co.ichain.luigi2.vo.UsersVo;

/**
 * UserMapper
 * 
 * @author : [AOT] g.kim
 * @createdAt : 2021-07-28
 * @updatedAt : 2021-07-28
 */
@Repository
@Luigi2Mapper
public interface UserMapper {

  Integer updateLoginUser(UsersVo param);

  List<AuthoritiesVo> getLoginUserAuth(UsersVo param);

  List<AuthoritiesVo> getApiAuth(@Param("tenantId") Integer tenantId, @Param("roleId") String roleId);

  List<NayoseResultVo> selectNayoseCustomerMatch(Map<String, Object> param);

  List<NayoseResultVo> selectNayoseCustomerIndividualPartialMatch(Map<String, Object> param);

  TenantsVo getExternalApiTenant(@Param("apiKey") String param);

}
