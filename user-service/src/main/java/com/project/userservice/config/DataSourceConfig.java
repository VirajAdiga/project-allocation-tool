package com.project.userservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Dotenv dotenv;

    private String getDatabaseHost(){
        return dotenv.get("DATABASE_HOST");
    }

    private String getDatabasePort(){
        return dotenv.get("DATABASE_PORT");
    }

    private String getDatabaseName(){
        return dotenv.get("DATABASE_NAME");
    }

    private String getDatabaseUsername(){
        return dotenv.get("DATABASE_USERNAME");
    }

    private String getDatabasePassword(){
        return dotenv.get("DATABASE_PASSWORD");
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
        em.setPackagesToScan("com.project.userservice.entities"); // Set entity package

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect"); // Set dialect

        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }
}
