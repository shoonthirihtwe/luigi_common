package jp.co.ichain.luigi2.config.security.cognito;

import static com.nimbusds.jose.JWSAlgorithm.RS256;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import com.google.protobuf.InvalidProtocolBufferException;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

/**
 * CognitoConfig
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@Configuration
public class CognitoConfig {

  @Autowired
  private JwtConfiguration jwtConfiguration;

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Bean
  @Scope(value = "prototype")
  public ConfigurableJWTProcessor<SecurityContext> configurableJwtProcessor(Integer tenantId)
      throws MalformedURLException, InstantiationException, IllegalAccessException,
      SecurityException, InvalidProtocolBufferException {
    ResourceRetriever resourceRetriever = new DefaultResourceRetriever(
        jwtConfiguration.getConnectionTimeout(), jwtConfiguration.getReadTimeout());
    URL jwkSetUrl = new URL(jwtConfiguration.getJwkUrl(tenantId));
    JWKSource keySource = new RemoteJWKSet(jwkSetUrl, resourceRetriever);
    ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
    JWSKeySelector keySelector = new JWSVerificationKeySelector(RS256, keySource);
    jwtProcessor.setJWSKeySelector(keySelector);
    return jwtProcessor;
  }
}
