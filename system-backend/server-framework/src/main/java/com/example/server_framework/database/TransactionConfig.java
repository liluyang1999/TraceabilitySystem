package com.example.server_framework.database;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@AutoConfigureAfter({DruidConfig.class})
@EnableTransactionManagement
@Configuration
public class TransactionConfig implements TransactionManagementConfigurer {

    @Autowired
    private DruidDataSource dataSource;

    @Override
    @NonNull
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
