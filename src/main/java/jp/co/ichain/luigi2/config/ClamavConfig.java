package jp.co.ichain.luigi2.config;

import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import fi.solita.clamav.ClamAVClient;

@Configuration
public class ClamavConfig {

  @Autowired
  Environment environment;

  @Value("${clamav.host.name}")
  private String hostName;

  @Value("${clamav.port}")
  private int port;

  @Bean
  public ClamAVClient makeClamAvClient() throws UnknownHostException {

    return new ClamAVClient(hostName, port);
  }
}
