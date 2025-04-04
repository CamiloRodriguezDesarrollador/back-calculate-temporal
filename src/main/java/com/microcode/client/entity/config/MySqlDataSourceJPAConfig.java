package com.microcode.client.entity.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.microcode.client.dao.mysql",
        entityManagerFactoryRef = "writingEntityManagerFactory",
        transactionManagerRef = "writingTransactionManager"
)
public class MySqlDataSourceJPAConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean writingEntityManagerFactory(
            @Qualifier("writingDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.microcode.client.entity.mysql")
                .build();
    }

    @Bean
    public PlatformTransactionManager writingTransactionManager(
            @Qualifier("writingEntityManagerFactory") LocalContainerEntityManagerFactoryBean writingEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(writingEntityManagerFactory.getObject()));
    }
}