package com.scloudic.rabbitframework.jbatis.springboot.configure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RabbitJbatisProperties.RABBIT_JBATIS_PREFIX)
public class RabbitJbatisProperties {
    public static final String RABBIT_JBATIS_PREFIX = "rabbit.jbatis";
    private DataSourceFactoryType dataSourceFactoryType = DataSourceFactoryType.SIMPLE;
    private List<String> entityPackages;
    private List<String> mapperPackages;
    private Map<String, String> cacheMap = null;
    private Map<String, DataSourceProperties> dataSourceBeans = new HashMap<String, DataSourceProperties>();

    public Map<String, DataSourceProperties> getDataSourceBeans() {
        return dataSourceBeans;
    }

    public void setDataSourceBeans(Map<String, DataSourceProperties> dataSourceBeans) {
        this.dataSourceBeans = dataSourceBeans;
    }

    public List<String> getEntityPackages() {
        return entityPackages;
    }

    public void setEntityPackages(List<String> entityPackages) {
        this.entityPackages = entityPackages;
    }

    public List<String> getMapperPackages() {
        return mapperPackages;
    }

    public void setMapperPackages(List<String> mapperPackages) {
        this.mapperPackages = mapperPackages;
    }

    public DataSourceFactoryType getDataSourceFactoryType() {
        return dataSourceFactoryType;
    }

    public void setDataSourceFactoryType(DataSourceFactoryType dataSourceFactoryType) {
        this.dataSourceFactoryType = dataSourceFactoryType;
    }

    public Map<String, String> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(Map<String, String> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public enum DataSourceFactoryType {
        SIMPLE, RANDOM, MULTI, MASTERSLAVE
    }
}
