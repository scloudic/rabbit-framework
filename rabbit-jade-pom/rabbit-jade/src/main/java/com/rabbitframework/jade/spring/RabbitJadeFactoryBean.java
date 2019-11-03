package com.rabbitframework.jade.spring;

import static org.springframework.util.Assert.notNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.builder.XMLConfigBuilder;
import com.rabbitframework.jade.dataaccess.DataSourceBean;
import com.rabbitframework.jade.dataaccess.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import com.rabbitframework.jade.RabbitJadeFactory;
import com.rabbitframework.jade.RabbitJadeFactoryBuilder;
import com.rabbitframework.jade.cache.Cache;
import com.rabbitframework.jade.dataaccess.datasource.DataSourceFactory;
import com.tjzq.commons.utils.StringUtils;

public class RabbitJadeFactoryBean
        implements FactoryBean<RabbitJadeFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJadeFactoryBean.class);
    private RabbitJadeFactoryBuilder rabbitJadeFactoryBuilder = new RabbitJadeFactoryBuilder();
    private RabbitJadeFactory rabbitJadeFactory;
    private Resource configLocation;
    private Properties configurationProperties;
    private DataSourceFactory dataSourceFactory;
    private Map<String, DataSourceBean> dataSourceMap;
    private Map<String, Cache> cacheMap;
    private String entityPackages;
    private String mapperPackages;
    private boolean failFast;

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public void setRabbitJadeFactoryBuilder(RabbitJadeFactoryBuilder rabbitJadeFactoryBuilder) {
        this.rabbitJadeFactoryBuilder = rabbitJadeFactoryBuilder;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
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
    public RabbitJadeFactory getObject() throws Exception {
        if (this.rabbitJadeFactory == null) {
            afterPropertiesSet();
        }
        return this.rabbitJadeFactory;
    }

    @Override
    public Class<? extends RabbitJadeFactory> getObjectType() {
        return this.rabbitJadeFactory == null ? RabbitJadeFactory.class : this.rabbitJadeFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(dataSourceMap, "Property 'dataSourceMap' is required");
        notNull(rabbitJadeFactoryBuilder, "Property 'RabbitJadeFactoryBuilder' is required");
        notNull(dataSourceFactory, "Property 'dataSourceFactory' is required");
        this.rabbitJadeFactory = buildRabbitJadeFactory();
    }

    public RabbitJadeFactory buildRabbitJadeFactory() throws Exception {
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
        String[] entityPackageNames = StringUtils.tokenizeToStringArray(entityPackages);
        configuration.addEntitys(entityPackageNames);
        String[] mapperPackageNames = StringUtils.tokenizeToStringArray(mapperPackages);
        configuration.addMappers(mapperPackageNames);
        configuration.addCaches(cacheMap);
        return rabbitJadeFactoryBuilder.build(configuration);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (failFast && event instanceof ContextRefreshedEvent) {
            this.rabbitJadeFactory.getConfiguration().getMappedStatementNames();
        }
    }
}
