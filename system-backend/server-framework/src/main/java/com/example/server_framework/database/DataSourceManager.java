package com.example.server_framework.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Dynamic Data Source
 */
public class DataSourceManager extends AbstractRoutingDataSource {

    public DataSourceManager(DataSource defaultTargetDataSource,
                             Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }

    static class DataSourceContextHolder {

        public static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);

        private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

        public static void setDataSourceType(String dataSourceType) {
            logger.info("Change to {} database", dataSourceType);
            CONTEXT_HOLDER.set(dataSourceType);
        }

        public static String getDataSourceType() {
            return CONTEXT_HOLDER.get();
        }

        public static void clearDataSourceType() {
            CONTEXT_HOLDER.remove();
        }
    }
}

