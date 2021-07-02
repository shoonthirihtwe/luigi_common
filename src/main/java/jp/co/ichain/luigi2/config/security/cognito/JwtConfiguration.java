package jp.co.ichain.luigi2.config.security.cognito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.google.protobuf.InvalidProtocolBufferException;
import jp.co.ichain.luigi2.resources.TenantResources;

/**
 * JwtConfiguration
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@Component
@ConfigurationProperties(prefix = "com.kb.jwt.aws")
public class JwtConfiguration {

  @Autowired
  TenantResources tenantResources;

  private String identityPoolId;
  private String jwkUrl;
  private String region = "ap-northeast-1";
  private String userNameField = "sub";
  private int connectionTimeout = 2000;
  private int readTimeout = 2000;
  private String httpHeader = "Authorization";


  public JwtConfiguration() {}

  public String getJwkUrl(Integer tenantId) throws InstantiationException, IllegalAccessException,
      SecurityException, InvalidProtocolBufferException {
    return this.jwkUrl != null && !this.jwkUrl.isEmpty() ? this.jwkUrl
        : String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json",
            this.region, tenantResources.get(tenantId).getUserPoolId());
  }

  public String getCognitoIdentityPoolUrl(Integer tenantId) throws InstantiationException,
      IllegalAccessException, SecurityException, InvalidProtocolBufferException {
    return String.format("https://cognito-idp.%s.amazonaws.com/%s", this.region,
        tenantResources.get(tenantId).getUserPoolId());
  }

  public String getUserPoolId(Integer tenantId) throws InstantiationException,
      IllegalAccessException, SecurityException, InvalidProtocolBufferException {
    return tenantResources.get(tenantId).getUserPoolId();
  }

  public String getIdentityPoolId() {
    return identityPoolId;
  }

  public void setIdentityPoolId(String identityPoolId) {
    this.identityPoolId = identityPoolId;
  }

  public void setJwkUrl(String jwkUrl) {
    this.jwkUrl = jwkUrl;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getUserNameField() {
    return userNameField;
  }

  public void setUserNameField(String userNameField) {
    this.userNameField = userNameField;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public String getHttpHeader() {
    return httpHeader;
  }

  public void setHttpHeader(String httpHeader) {
    this.httpHeader = httpHeader;
  }
}
