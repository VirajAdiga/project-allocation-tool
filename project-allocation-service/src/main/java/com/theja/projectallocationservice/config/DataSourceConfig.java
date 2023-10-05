package com.theja.projectallocationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment environment;

    private String getDatabaseHost(){
        return environment.getProperty("DATABASE_HOST");
    }

    private String getDatabasePort(){
        return environment.getProperty("DATABASE_PORT");
    }

    private String getDatabaseName(){
        return environment.getProperty("DATABASE_NAME");
    }

    private String getDatabaseUsername(){
        return environment.getProperty("DATABASE_USERNAME");
    }

    private String getDatabasePassword(){
        return environment.getProperty("DATABASE_PASSWORD");
    }

    private String getDatabaseUrl(){
        return String.format("%s:%s/%s", getDatabaseHost(), getDatabasePort(), getDatabaseName());
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(getDatabaseUrl());
        dataSource.setUsername(getDatabaseUsername());
        dataSource.setPassword(getDatabasePassword());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.theja.projectallocationservice.entities"); // Set entity package

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect"); // Set dialect

        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }
}
