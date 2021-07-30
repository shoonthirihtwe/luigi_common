package jp.co.ichain.luigi2.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jp.co.ichain.luigi2.config.security.cognito.AwsCognitoIdTokenProcessor;
import jp.co.ichain.luigi2.config.security.cognito.AwsCognitoJwtAuthFilter;
import jp.co.ichain.luigi2.mapper.CommonMapper;
import jp.co.ichain.luigi2.resources.TenantResources;
import jp.co.ichain.luigi2.util.CollectionUtils;
import lombok.val;

/**
 * Security設定
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-07-02
 * @updatedAt : 2021-07-02
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "jp.co.ichain.luigi2.config.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  SecurityUserDetailsService securityUserDetailsService;
  @Autowired
  RestAuthenticationFailureEntryPoint restAuthenticationEntryPoint;
  @Autowired
  AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;
  @Autowired
  TenantResources tenantResources;
  @Autowired
  CommonMapper commonMapper;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html",
        "/webjars/**", "/swagger/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(new AwsCognitoJwtAuthFilter(awsCognitoIdTokenProcessor),
        UsernamePasswordAuthenticationFilter.class);
    http.csrf().disable().rememberMe();
    http.logout().disable();
    http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);

    http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()
        .cors();
    http.authorizeRequests().antMatchers("/login", "/logout", "/err/**", "/p/**", "/actuator/**")
        .permitAll();
    http.authorizeRequests().antMatchers("/u/**").authenticated();

    for (val functionId : commonMapper.selectFunctionId()) {
      http.authorizeRequests().antMatchers("/" + functionId).hasAnyAuthority(functionId);
    }

    http.authorizeRequests().anyRequest().denyAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(securityUserDetailsService);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource()
      throws InstantiationException, IllegalAccessException, SecurityException {

    CorsConfiguration configuration = new CorsConfiguration();
    // CORS
    for (val tenantVo : CollectionUtils.safe(tenantResources.getAll())) {
      configuration.addAllowedOrigin("https://" + tenantVo.getSiteUrl());
    }
    configuration.addAllowedOrigin("http://localhost:3000");

    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }
}
