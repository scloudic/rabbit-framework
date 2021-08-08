package com.scloudic.rabbitframework.jbatis.springboot.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RabbitJbatisProperties.RABBIT_JBATIS_PREFIX)
public class RabbitJbatisProperties {
    public static final String RABBIT_JBATIS_PREFIX = "rabbit.jbatis";
    private DataSourceFactoryType dataSourceFactoryType = DataSourceFactoryType.SIMPLE;
    private String entityPackages;
    private String mapperPackages;
    private Map<String, String> mapperPackageMap;
    private Map<String, DataSourceProperties> dataSources = new HashMap<String, DataSourceProperties>();
    private Map<String, String> dataSourceBeans = new HashMap<String, String>();

    public Map<String, DataSourceProperties> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, DataSourceProperties> dataSources) {
        this.dataSources = dataSources;
    }

    public String getEntityPackages() {
        return entityPackages;
    }

    public void setEntityPackages(String entityPackages) {
        this.entityPackages = entityPackages;
    }

    public String getMapperPackages() {
        return mapperPackages;
    }

    public void setMapperPackages(String mapperPackages) {
        this.mapperPackages = mapperPackages;
    }

    public Map<String, String> getDataSourceBeans() {
        return dataSourceBeans;
    }

    public void setDataSourceBeans(Map<String, String> dataSourceBeans) {
        this.dataSourceBeans = dataSourceBeans;
    }

    public DataSourceFactoryType getDataSourceFactoryType() {
        return dataSourceFactoryType;
    }

    public void setDataSourceFactoryType(DataSourceFactoryType dataSourceFactoryType) {
        this.dataSourceFactoryType = dataSourceFactoryType;
    }

    public static class DataSourceProperties {
        private String className;
        private String dialect = "mysql";
        private boolean transaction = true;
        private Map<String, Object> params = new HashMap<String, Object>();

        public void setDialect(String dialect) {
            this.dialect = dialect;
        }

        public String getDialect() {
            return dialect;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        public void setTransaction(boolean transaction) {
            this.transaction = transaction;
        }

        public boolean isTransaction() {
            return transaction;
        }
    }

    public Map<String, String> getMapperPackageMap() {
        return mapperPackageMap;
    }

    public void setMapperPackageMap(Map<String, String> mapperPackageMap) {
        this.mapperPackageMap = mapperPackageMap;
    }

    public enum DataSourceFactoryType {
        SIMPLE, RANDOM, MULTI, MASTERSLAVE
    }
}
