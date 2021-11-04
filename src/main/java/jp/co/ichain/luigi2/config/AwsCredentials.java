package jp.co.ichain.luigi2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;

/**
 * AWS認証
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-30
 * @updatedAt : 2021-06-30
 */
@Configuration
public class AwsCredentials {

  private final String accessKey;
  private final String secretKey;

  AwsCredentials(@Value("${aws.credential.access.key}") String accessKey,
      @Value("${aws.credential.secret.key}") String secretKey) {

    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }

  @Bean
  public AWSCredentialsProvider makeAwsCredentials() {
    return new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
        new SystemPropertiesCredentialsProvider(), WebIdentityTokenCredentialsProvider.create(),
        new InstanceProfileCredentialsProvider(true),
        new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)),
        new EC2ContainerCredentialsProviderWrapper());
  }
}
