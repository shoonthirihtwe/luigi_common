package jp.co.ichain.luigi2.batch;

import java.util.List;
import jp.co.ichain.luigi2.vo.TenantsVo;

public interface BatchService {

  public void run(List<TenantsVo> tenantList);
}
