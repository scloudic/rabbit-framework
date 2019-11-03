package com.rabbitframework.jade.springboot.configure;

import com.rabbitframework.jade.RabbitJadeFactory;
import com.rabbitframework.jade.dataaccess.DataSourceBean;
import com.rabbitframework.jade.dataaccess.datasource.*;
import com.rabbitframework.jade.spring.RabbitJadeFactoryBean;
import com.tjzq.commons.utils.ClassUtils;
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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(RabbitJadeProperties.class)
public class RabbitJadeAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJadeAutoConfiguration.class);
    private final RabbitJadeProperties rabbitJadeProperties;
    private final ApplicationContext applicationContext;

    public RabbitJadeAutoConfiguration(ApplicationContext applicationContext,
                                       RabbitJadeProperties rabbitJadeProperties) {
        this.applicationContext = applicationContext;
        this.rabbitJadeProperties = rabbitJadeProperties;
        registerBean((ConfigurableApplicationContext) applicationContext);
        registerTransactionManagement((ConfigurableApplicationContext) applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory dataSourceFactory() {
        RabbitJadeProperties.DataSourceFactoryType dataSourceFactoryType = rabbitJadeProperties
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

    @Bean("rabbitJadeFactory")
    @ConditionalOnMissingBean
    public RabbitJadeFactory rabbitJadeFactory() throws Exception {
        RabbitJadeFactoryBean rabbitJadeFactoryBean = new RabbitJadeFactoryBean();
        rabbitJadeFactoryBean.setDataSourceFactory(dataSourceFactory());
        rabbitJadeFactoryBean.setEntityPackages(rabbitJadeProperties.getEntityPackages());
        rabbitJadeFactoryBean.setMapperPackages(rabbitJadeProperties.getMapperPackages());
        Map<String, DataSourceBean> dataSourceMap = new HashMap<>();
        Map<String, String> dataSourceBeans = rabbitJadeProperties.getDataSourceBeans();
        dataSourceBeans.forEach((k, v) -> {
            DataSource dataSource = (DataSource) applicationContext.getBean(v);
            String dialect = rabbitJadeProperties.getDataSources().get(v).getDialect();
            DataSourceBean dataSourceBean = new DataSourceBean();
            dataSourceBean.setDataSource(dataSource);
            dataSourceBean.setDialect(dialect);
            dataSourceMap.put(k, dataSourceBean);
        });
        rabbitJadeFactoryBean.setDataSourceMap(dataSourceMap);
        return rabbitJadeFactoryBean.getObject();
    }

    private void registerBean(ConfigurableApplicationContext applicationContext) {
        Map<String, RabbitJadeProperties.DataSourceProperties> dataSources = rabbitJadeProperties.getDataSources();
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
        Map<String, String> dataSourceBeans = rabbitJadeProperties.getDataSourceBeans();
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