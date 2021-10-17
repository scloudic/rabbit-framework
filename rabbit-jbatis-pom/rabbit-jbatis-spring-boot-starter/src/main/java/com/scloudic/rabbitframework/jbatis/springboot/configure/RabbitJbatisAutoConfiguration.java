package com.scloudic.rabbitframework.jbatis.springboot.configure;

import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.jbatis.RabbitJbatisFactory;
import com.scloudic.rabbitframework.jbatis.cache.Cache;
import com.scloudic.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.scloudic.rabbitframework.jbatis.dataaccess.datasource.*;
import com.scloudic.rabbitframework.jbatis.spring.RabbitJbatisFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(RabbitJbatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class RabbitJbatisAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJbatisAutoConfiguration.class);
    private final RabbitJbatisProperties rabbitJbatisProperties;
    private final ApplicationContext applicationContext;

    public RabbitJbatisAutoConfiguration(ApplicationContext applicationContext,
                                         RabbitJbatisProperties rabbitJbatisProperties) {
        this.applicationContext = applicationContext;
        this.rabbitJbatisProperties = rabbitJbatisProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory dataSourceFactory() {
        RabbitJbatisProperties.DataSourceFactoryType dataSourceFactoryType = rabbitJbatisProperties
                .getDataSourceFactoryType();
        DataSourceFactory dataSourceFactory = null;
        switch (dataSourceFactoryType) {
            case SIMPLE:
                dataSourceFactory = new SimpleDataSourceFactory();
                break;
            case MULTI:
                dataSourceFactory = new MultiDataSourceFactory();
                break;
            case RANDOM:
                dataSourceFactory = new RandomDataSourceFactory();
                break;
            case MASTERSLAVE:
                dataSourceFactory = new MasterSlaveDataSourceFactory();
                break;
        }
        return dataSourceFactory;
    }

    @Bean("rabbitJbatisFactory")
    @ConditionalOnMissingBean
    public RabbitJbatisFactory rabbitJbatisFactory() throws Exception {
        logger.debug("init RabbitJbatisFactory");
        RabbitJbatisFactoryBean rabbitJbatisFactoryBean = new RabbitJbatisFactoryBean();
        Map<String, String> cacheMap = rabbitJbatisProperties.getCacheMap();
        if (cacheMap != null) {
            Map<String, Cache> saveCacheMap = new HashMap<>();
            for (Map.Entry<String, String> entry : cacheMap.entrySet()) {
                String id = entry.getKey();
                String name = entry.getValue();
                Cache cache = (Cache) ClassUtils.newInstance(ClassUtils.classForName(name), id);
                saveCacheMap.put(id, cache);
            }
            if (saveCacheMap.size() > 0) {
                rabbitJbatisFactoryBean.setCacheMap(saveCacheMap);
            }
        }
        rabbitJbatisFactoryBean.setDataSourceFactory(dataSourceFactory());
        rabbitJbatisFactoryBean.setEntityPackages(rabbitJbatisProperties.getEntityPackages());
        rabbitJbatisFactoryBean.setMapperPackages(rabbitJbatisProperties.getMapperPackages());
        Map<String, DataSourceBean> dataSourceMap = new HashMap<>();
        Map<String, DataSourceProperties> dataSourcePropertiesMap = rabbitJbatisProperties.getDataSourceBeans();
        if (dataSourcePropertiesMap.size() > 0) {
            for (Map.Entry<String, DataSourceProperties> entry : dataSourcePropertiesMap.entrySet()) {
                DataSourceProperties dataSourceProperties = entry.getValue();
                DataSourceBean dataSourceBean = new DataSourceBean();
                DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceProperties.getBeanName());
                dataSourceBean.setDataSource(dataSource);
                dataSourceBean.setDialect(dataSourceProperties.getDialect());
                dataSourceMap.put(entry.getKey(), dataSourceBean);
            }
        } else {
            DataSourceBean dataSourceBean = new DataSourceBean();
            DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
            dataSourceBean.setDataSource(dataSource);
            dataSourceBean.setDialect("mysql");
            dataSourceMap.put("default", dataSourceBean);
        }
        rabbitJbatisFactoryBean.setDataSourceMap(dataSourceMap);
        return rabbitJbatisFactoryBean.getObject();
    }
}