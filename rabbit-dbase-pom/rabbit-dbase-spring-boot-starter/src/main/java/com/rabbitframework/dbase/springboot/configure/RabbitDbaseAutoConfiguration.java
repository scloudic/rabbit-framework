package com.rabbitframework.dbase.springboot.configure;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.rabbitframework.dbase.RabbitDbaseFactory;
import com.rabbitframework.dbase.dataaccess.DataSourceBean;
import com.rabbitframework.dbase.dataaccess.datasource.DataSourceFactory;
import com.rabbitframework.dbase.dataaccess.datasource.MasterSlaveDataSourceFactory;
import com.rabbitframework.dbase.dataaccess.datasource.MultiDataSourceFactory;
import com.rabbitframework.dbase.dataaccess.datasource.RandomDataSourceFactory;
import com.rabbitframework.dbase.dataaccess.datasource.SimpleDataSourceFactory;
import com.rabbitframework.dbase.spring.RabbitDbaseFactoryBean;
import com.tjzq.commons.utils.ClassUtils;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(RabbitDbaseProperties.class)
public class RabbitDbaseAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitDbaseAutoConfiguration.class);
    private final RabbitDbaseProperties rabbitDbaseProperties;
    private final ApplicationContext applicationContext;

    public RabbitDbaseAutoConfiguration(ApplicationContext applicationContext,
                                        RabbitDbaseProperties rabbitDbaseProperties) {
        this.applicationContext = applicationContext;
        this.rabbitDbaseProperties = rabbitDbaseProperties;
        registerBean((ConfigurableApplicationContext) applicationContext);
        registerTransactionManagement((ConfigurableApplicationContext) applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory dataSourceFactory() {
        RabbitDbaseProperties.DataSourceFactoryType dataSourceFactoryType = rabbitDbaseProperties
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

    @Bean("rabbitDbaseFactory")
    @ConditionalOnMissingBean
    public RabbitDbaseFactory rabbitDbaseFactory() throws Exception {
        RabbitDbaseFactoryBean rabbitDbaseFactoryBean = new RabbitDbaseFactoryBean();
        rabbitDbaseFactoryBean.setDataSourceFactory(dataSourceFactory());
        rabbitDbaseFactoryBean.setEntityPackages(rabbitDbaseProperties.getEntityPackages());
        rabbitDbaseFactoryBean.setMapperPackages(rabbitDbaseProperties.getMapperPackages());
        Map<String, DataSourceBean> dataSourceMap = new HashMap<>();
        Map<String, String> dataSourceBeans = rabbitDbaseProperties.getDataSourceBeans();
        dataSourceBeans.forEach((k, v) -> {
            DataSource dataSource = (DataSource) applicationContext.getBean(v);
            String dialect = rabbitDbaseProperties.getDataSources().get(v).getDialect();
            DataSourceBean dataSourceBean = new DataSourceBean();
            dataSourceBean.setDataSource(dataSource);
            dataSourceBean.setDialect(dialect);
            dataSourceMap.put(k, dataSourceBean);
        });
        rabbitDbaseFactoryBean.setDataSourceMap(dataSourceMap);
        return rabbitDbaseFactoryBean.getObject();
    }

    private void registerBean(ConfigurableApplicationContext applicationContext) {
        Map<String, RabbitDbaseProperties.DataSourceProperties> dataSources = rabbitDbaseProperties.getDataSources();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        dataSources.forEach((k, v) -> {
            String className = v.getClassName();
            Map<String, Object> map = v.getParams();
            registerDataSource(k, className, map, beanFactory);
        });
    }

    private void registerDataSource(String key, String className, Map<String, Object> params,
                                    BeanDefinitionRegistry registry) {
        try {
            Class<?> dataSource = ClassUtils.classForName(className);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSource);
            params.forEach((k, v) -> {
                beanDefinitionBuilder.addPropertyValue(k, v);
            });
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            beanDefinition.setInitMethodName("init");
            beanDefinition.setDestroyMethodName("close");
            registry.registerBeanDefinition(key, beanDefinition);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void registerTransactionManagement(ConfigurableApplicationContext applicationContext) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        Map<String, String> dataSourceBeans = rabbitDbaseProperties.getDataSourceBeans();
        dataSourceBeans.forEach((k, v) -> {
            DataSource dataSource = (DataSource) applicationContext.getBean(v);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
            beanDefinitionBuilder.addPropertyValue("dataSource", dataSource);
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            registry.registerBeanDefinition(v + "Tx", beanDefinition);
            // registry.registerBeanDefinition("transactionManager", beanDefinition);
        });
    }
}