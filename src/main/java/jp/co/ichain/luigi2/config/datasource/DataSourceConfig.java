package jp.co.ichain.luigi2.config.datasource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import com.zaxxer.hikari.HikariDataSource;
import lombok.val;

/**
 * DB dataSource設定
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-27
 * @updatedAt : 2021-05-27
 */
@Configuration
@MapperScan(value = "jp.co.ichain.luigi2.mapper", annotationClass = Luigi2Mapper.class,
    sqlSessionFactoryRef = "luigi2SqlSessionFactory")
public class DataSourceConfig {

  @Value("${mybatis.type-aliases-package}")
  private String typeAliasPackage;

  @Autowired
  Environment env;

  @Autowired
  ResourcePatternResolver resourcePatternResolver;

  @Bean("dataSourceInitializer")
  public DataSourceInitializer dataSourceInitializer(
      @Qualifier("luigi2DataSource") DataSource datasource) throws IOException {
    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

    if (env.acceptsProfiles(Profiles.of("test"))) {

      resourceDatabasePopulator.setSqlScriptEncoding("UTF-8");
      resourceDatabasePopulator.setContinueOnError(true);
      resourceDatabasePopulator.addScript(new ClassPathResource("DDL/current_DDL_LG2.sql"));
      resourceDatabasePopulator.addScript(new ClassPathResource("test_sql/init_data_clear.sql"));
      Resource[] resources = resourcePatternResolver.getResources("classpath:Seed/*.sql");
      for (Resource resource : resources) {
        resourceDatabasePopulator.addScript(resource);
      }

      resourceDatabasePopulator.addScript(new ClassPathResource("Service_instance_base_data/common.sql"));
    }
    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(datasource);
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }

  /**
   * Session Factory
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param dataSource
   * @param applicationContext
   * @return
   * @throws Exception
   */
  @Bean(name = "luigi2SqlSessionFactory")
  public SqlSessionFactory luigi2SqlSessionFactoryBean(
      @Autowired @Qualifier("luigi2DataSource") DataSource dataSource,
      ApplicationContext applicationContext) throws Exception {
    val factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setVfs(SpringBootVFS.class);
    factoryBean.setTypeAliasesPackage(typeAliasPackage);
    org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
    config.setMapUnderscoreToCamelCase(true);
    factoryBean.setConfiguration(config);
    factoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/**/*.xml"));
    return factoryBean.getObject();
  }

  @Bean(name = "luigi2DataSource")
  public DataSource dataSource(
      @Autowired @Qualifier("luigi2RoutingDataSource") DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }

  /**
   * routing DataSource
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param masterDataSourceProperty
   * @param slaveDataSourceProperty
   * @return
   */
  @Bean(name = "luigi2RoutingDataSource")
  public DataSource routingDataSource(
      @Autowired @Qualifier("luigi2MasterDataSource") DataSource masterDataSource,
      @Autowired @Qualifier("luigi2SlaveDataSource") DataSource slaveDataSource) {

    DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();

    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put("master", masterDataSource);
    dataSourceMap.put("slave", slaveDataSource);
    routingDataSource.setTargetDataSources(dataSourceMap);
    routingDataSource.setDefaultTargetDataSource(masterDataSource);

    return routingDataSource;
  }

  /**
   * Master dataSource
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @return
   */
  @Bean(name = "luigi2MasterDataSource")
  @ConfigurationProperties(prefix = "spring.luigi2.datasource.master")
  public DataSource createMasterDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  /**
   * Slave dataSource
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @return
   */
  @Bean(name = "luigi2SlaveDataSource")
  @ConfigurationProperties(prefix = "spring.luigi2.datasource.slave")
  public DataSource createSlaveDataSource() {
    return DataSourceBuilder.create().build();
  }

  /**
   * SqlSession
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param luigi2SqlSessionFactory
   * @return
   */
  @Bean(name = "luigi2SqlSession")
  public SqlSession sqlSession(
      @Autowired @Qualifier("luigi2SqlSessionFactory") SqlSessionFactory luigi2SqlSessionFactory) {
    return new SqlSessionTemplate(luigi2SqlSessionFactory);
  }

  /**
   * Transaction Manager
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-05-27
   * @updatedAt : 2021-05-27
   * @param dataSource
   * @return
   */
  @Bean(name = "luigi2TransactionManager")
  public DataSourceTransactionManager transactionManager(
      @Autowired @Qualifier("luigi2DataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
