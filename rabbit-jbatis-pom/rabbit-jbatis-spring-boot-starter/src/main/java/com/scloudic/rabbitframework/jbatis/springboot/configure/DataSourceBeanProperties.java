package com.scloudic.rabbitframework.jbatis.springboot.configure;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源bean属性配置
 *
 * @author justin
 * @since JDK1.8
 */
public class DataSourceBeanProperties {
    private String defaultDataSource;
    private String routingDatasourceClass;
    private Map<String, String> dataSourceName = new HashMap<String, String>();

    public String getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(String defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public Map<String, String> getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(Map<String, String> dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    
    public String getRoutingDatasourceClass() {
        return routingDatasourceClass;
    }

    public void setRoutingDatasourceClass(String routingDatasourceClass) {
        this.routingDatasourceClass = routingDatasourceClass;
    }
}
