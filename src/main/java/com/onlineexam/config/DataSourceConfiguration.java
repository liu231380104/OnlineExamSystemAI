package com.onlineexam.config;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class DataSourceConfiguration {
  @Value("${spring.datasource.driver-class-name}")
  private String jdbcDriver;
  @Value("${spring.datasource.url}")
  private String jdbcUrl;
  @Value("${spring.datasource.username}")
  private String jdbcUsername;
  @Value("${spring.datasource.password}")
  private String jdbcPassword;

  @Bean(name="dataSource")
  public DruidDataSource createDataSource() throws Exception {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName(jdbcDriver);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(jdbcUsername);
    dataSource.setPassword(jdbcPassword);

    // 基本配置
    dataSource.setDefaultAutoCommit(false);
    dataSource.setBreakAfterAcquireFailure(true);
    dataSource.setFailFast(true);
    dataSource.setConnectionErrorRetryAttempts(0);
    
    return dataSource;
  }
}
