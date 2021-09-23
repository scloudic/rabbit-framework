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
    private Map<String, String> cacheMap = null;
    private Map<String, DataSourceProperties> dataSources = new HashMap<String, DataSourceProperties>();
    private DataSourceBeanProperties dataSourceBeans;
    private TransactionProperties transaction = new TransactionProperties();

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

    public DataSourceFactoryType getDataSourceFactoryType() {
        return dataSourceFactoryType;
    }

    public void setDataSourceFactoryType(DataSourceFactoryType dataSourceFactoryType) {
        this.dataSourceFactoryType = dataSourceFactoryType;
    }

    public DataSourceBeanProperties getDataSourceBeans() {
        return dataSourceBeans;
    }

    public void setDataSourceBeans(DataSourceBeanProperties dataSourceBeans) {
        this.dataSourceBeans = dataSourceBeans;
    }

    public Map<String, String> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(Map<String, String> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public TransactionProperties getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionProperties transaction) {
        this.transaction = transaction;
    }

    public enum DataSourceFactoryType {
        SIMPLE, RANDOM, MULTI, MASTERSLAVE
    }
}
