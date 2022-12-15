package com.example.server_framework.database;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Database Connection Framework: Druid
 */
@ComponentScan(value = "com.example.server_framework.database")
@Configuration
public class DruidConfig {

    @Bean(name = "myDataSource")
    @Primary
    public DruidDataSource dataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.initiaize(dataSource);
    }
}
