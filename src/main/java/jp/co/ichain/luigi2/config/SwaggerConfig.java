package jp.co.ichain.luigi2.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 設定
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-31
 * @updatedAt : 2021-05-31
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  @Conditional(WebExistsCondition.class)
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any()).build().apiInfo(apiInfo()).enable(false);
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("API").description("API description").version("1.0").build();
  }

  @Primary
  @Bean
  @Conditional(WebExistsCondition.class)
  public SwaggerResourcesProvider swaggerResourcesProvider(
      InMemorySwaggerResourcesProvider defaultResourcesProvider) {
    return () -> {
      SwaggerResource wsResource = new SwaggerResource();
      wsResource.setName("api-specification");
      wsResource.setSwaggerVersion("2.0");
      wsResource.setLocation("/swagger-resources/swagger.json");
      List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
      resources.add(wsResource);
      return resources;
    };
  }

}
