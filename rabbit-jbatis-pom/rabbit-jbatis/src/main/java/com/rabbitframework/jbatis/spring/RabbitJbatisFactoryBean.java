package com.rabbitframework.jbatis.spring;

import static org.springframework.util.Assert.notNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.builder.XMLConfigBuilder;
import com.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.rabbitframework.jbatis.dataaccess.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import com.rabbitframework.jbatis.RabbitJbatisFactory;
import com.rabbitframework.jbatis.RabbitJbatisFactoryBuilder;
import com.rabbitframework.jbatis.cache.Cache;
import com.rabbitframework.jbatis.dataaccess.datasource.DataSourceFactory;
import com.rabbitframework.core.utils.StringUtils;

public class RabbitJbatisFactoryBean
        implements FactoryBean<RabbitJbatisFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJbatisFactoryBean.class);
    private RabbitJbatisFactoryBuilder rabbitJbatisFactoryBuilder = new RabbitJbatisFactoryBuilder();
    private RabbitJbatisFactory rabbitJbatisFactory;
    private Resource configLocation;
    private Properties configurationProperties;
    private DataSourceFactory dataSourceFactory;
    private Map<String, DataSourceBean> dataSourceMap;
    private Map<String, Cache> cacheMap;
    private Map<String, String> mapperPackageMap;
    private String entityPackages;
    private String mapperPackages;
    private boolean failFast;

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public void setRabbitJbatisFactoryBuilder(RabbitJbatisFactoryBuilder rabbitJbatisFactoryBuilder) {
        this.rabbitJbatisFactoryBuilder = rabbitJbatisFactoryBuilder;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public void setMapperPackageMap(Map<String, String> mapperPackageMap) {
        this.mapperPackageMap = mapperPackageMap;
    }

    public void setCacheMap(Map<String, Cache> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public void setEntityPackages(String entityPackages) {
        this.entityPackages = entityPackages;
    }

    public void setMapperPackages(String mapperPackages) {
        this.mapperPackages = mapperPackages;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public void setDataSourceMap(Map<String, DataSourceBean> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    @Override
    public RabbitJbatisFactory getObject() throws Exception {
        if (this.rabbitJbatisFactory == null) {
            afterPropertiesSet();
        }
        return this.rabbitJbatisFactory;
    }

    @Override
    public Class<? extends RabbitJbatisFactory> getObjectType() {
        return this.rabbitJbatisFactory == null ? RabbitJbatisFactory.class : this.rabbitJbatisFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(dataSourceMap, "Property 'dataSourceMap' is required");
        notNull(rabbitJbatisFactoryBuilder, "Property 'RabbitJbatisFactoryBuilder' is required");
        notNull(dataSourceFactory, "Property 'dataSourceFactory' is required");
        this.rabbitJbatisFactory = buildRabbitJbatisFactory();
    }

    public RabbitJbatisFactory buildRabbitJbatisFactory() throws Exception {
        Configuration configuration = null;
        XMLConfigBuilder configBuilder = null;
        if (configLocation != null) {
            configBuilder = new XMLConfigBuilder(configLocation.getInputStream(), configurationProperties);
            configuration = configBuilder.getConfiguration();
        } else {
            configuration = new Configuration();
            configuration.setVariables(configurationProperties);
        }
        if (configBuilder != null) {
            try {
                configBuilder.parse();
                logger.trace("Parsed configuration file: '" + this.configLocation + "'");
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            }
        }
        Environment environment = new Environment();
        for (Entry<String, DataSourceBean> entry : dataSourceMap.entrySet()) {
            String name = entry.getKey();
            DataSourceBean dataSource = entry.getValue();
            dataSourceFactory.addDataSource(name, dataSource);
            environment.addCacheDataSource(dataSource.getDataSource());
        }
        environment.setDataSourceFactory(dataSourceFactory);
        configuration.setEnvironment(environment);
        if (StringUtils.isNotBlank(entityPackages)) {
            String[] entityPackageNames = StringUtils.tokenizeToStringArray(entityPackages);
            configuration.addEntitys(entityPackageNames);
        }
        if (StringUtils.isNotBlank(mapperPackages)) {
            String[] mapperPackageNames = StringUtils.tokenizeToStringArray(mapperPackages);
            configuration.addMappers(mapperPackageNames, "");
        }
        if (mapperPackageMap != null) {
            for (Map.Entry<String, String> entry : mapperPackageMap.entrySet()) {
                String catalog = entry.getKey();
                String mapperPackages = entry.getValue();
                String[] mapperPackageNames = StringUtils.tokenizeToStringArray(mapperPackages);
                configuration.addMappers(mapperPackageNames, catalog);
            }
        }
        configuration.addCaches(cacheMap);
        return rabbitJbatisFactoryBuilder.build(configuration);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (failFast && event instanceof ContextRefreshedEvent) {
            this.rabbitJbatisFactory.getConfiguration().getMappedStatementNames();
        }
    }
}
