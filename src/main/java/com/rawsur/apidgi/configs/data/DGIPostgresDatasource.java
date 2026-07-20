package com.rawsur.apidgi.configs.data;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(entityManagerFactoryRef = "dgiEntityManagerFactory", transactionManagerRef = "dgiTransactionManager", basePackages = {
    "com.rawsur.apidgi.repositories.dgi" })
public class DGIPostgresDatasource {

  @Primary
  @Bean(name = "dgiDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.dgi")
  DataSource dataSourcedgi() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean(name = "dgiEntityManagerFactory")
  LocalContainerEntityManagerFactoryBean entityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("dgiDataSource") DataSource dataSource) {
    Map<String, Object> properties = new HashMap<>();
    String us = "dgi";

    properties.put("spring.datasource.dgi.jdbc-url", "jdbc:postgresql://192.168.88.95:5432/db_dgi_test");
    properties.put("spring.datasource.dgi.username", us);
    properties.put("spring.datasource.dgi.password", "dgi");
    properties.put("spring.jpa.show-sql", true);
    properties.put("spring.jpa.properties.hibernate.format_sql", true);
    properties.put("spring.jpa.properties.hibernate.default_schema", "public");
    properties.put("spring.jpa.hibernate.ddl-auto", "update");
    properties.put("hibernate.hbm2ddl.auto", "update");
    properties.put("hibernate.dialect",
        "org.hibernate.dialect.PostgreSQLDialect");
    return builder
        .dataSource(dataSource)
        .packages("com.rawsur.apidgi.models.dgi")
        .persistenceUnit("dgi")
        .properties(properties)
        .build();
  }

  @Primary
  @Bean(name = "dgiTransactionManager")
  PlatformTransactionManager transactionManager(
      @Qualifier("dgiEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
