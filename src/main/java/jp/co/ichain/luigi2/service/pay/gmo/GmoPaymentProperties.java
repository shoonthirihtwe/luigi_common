package jp.co.ichain.luigi2.service.pay.gmo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import jp.co.ichain.luigi2.util.CollectionUtils;
import lombok.val;

public class GmoPaymentProperties {

  private static GmoPaymentProperties me = null;

  private GmoPaymentProperties() {}

  public static GmoPaymentProperties getInstance() {
    if (me == null) {
      me = new GmoPaymentProperties();
    }

    return me;
  }

  @SuppressWarnings("serial")
  public Map<String, String> ERROR_MAP = new HashMap<String, String>() {
    {
      try {
        Properties properties = PropertiesLoaderUtils
            .loadProperties(new ClassPathResource("pay/gmo/error-code.properties"));

        for (val prop : CollectionUtils.safe(properties.entrySet())) {
          put((String) prop.getKey(), (String) prop.getValue());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };

  @SuppressWarnings("serial")
  public Map<String, String> ERROR_PAYMENT_RESULT_MAP = new HashMap<String, String>() {
    {
      try {
        Properties properties = PropertiesLoaderUtils
            .loadProperties(new ClassPathResource("pay/gmo/error-payment-result.properties"));

        for (val prop : CollectionUtils.safe(properties.entrySet())) {
          put((String) prop.getKey(), (String) prop.getValue());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
}
