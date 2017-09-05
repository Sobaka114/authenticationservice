package com.sasha.authenticationservice.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = {"com.sasha.authenticationservice.service.repository"})
@EnableTransactionManagement
public class ContextDbConfig {

    @Value("${database.driver.class_name}")
    private String databaseDriveClassName;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${hibernate.dialect}")
    private String databaseHibernateDialect;

    @Bean
    public DataSource dataSource() {
        //http://www.baeldung.com/spring-data-jpa-multiple-databases
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriveClassName);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("com.sasha.authenticationservice");

        Map<String, Object> jpaPropertiesMap = new HashMap<>();
        jpaPropertiesMap.put("hibernate.connection.pool_size", 10);
        jpaPropertiesMap.put("hibernate.connection.show_sql", true);
        jpaPropertiesMap.put("hibernate.dialect", databaseHibernateDialect);
        jpaPropertiesMap.put("hibernate.show_sql", true);
        emf.setJpaPropertyMap(jpaPropertiesMap);

        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean
    JpaTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

}
