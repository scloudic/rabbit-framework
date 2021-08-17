package com.scloudic.rabbitframework.jbatis.springboot.configure;

import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.jbatis.RabbitJbatisFactory;
import com.scloudic.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.scloudic.rabbitframework.jbatis.dataaccess.datasource.*;
import com.scloudic.rabbitframework.jbatis.spring.RabbitJbatisFactoryBean;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(RabbitJbatisProperties.class)
public class RabbitJbatisAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJbatisAutoConfiguration.class);
    private final RabbitJbatisProperties rabbitJbatisProperties;
    private final ApplicationContext applicationContext;

    public RabbitJbatisAutoConfiguration(ApplicationContext applicationContext,
                                         RabbitJbatisProperties rabbitJbatisProperties) {
        this.applicationContext = applicationContext;
        this.rabbitJbatisProperties = rabbitJbatisProperties;
        registerBean((ConfigurableApplicationContext) applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory dataSourceFactory() {
        RabbitJbatisProperties.DataSourceFactoryType dataSourceFactoryType = rabbitJbatisProperties
                .getDataSourceFactoryType();
        DataSourceBeanProperties dataSourceBeans = rabbitJbatisProperties.getDataSourceBeans();
        //如果是动态数据源此处设为简单数据源工厂
        if (StringUtils.isNotBlank(dataSourceBeans.getRoutingDatasourceClass())) {
            dataSourceFactoryType = RabbitJbatisProperties.DataSourceFactoryType.SIMPLE;
        }
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
        RabbitJbatisFactoryBean rabbitJbatisFactoryBean = new RabbitJbatisFactoryBean();
        rabbitJbatisFactoryBean.setDataSourceFactory(dataSourceFactory());
        rabbitJbatisFactoryBean.setEntityPackages(rabbitJbatisProperties.getEntityPackages());
        rabbitJbatisFactoryBean.setMapperPackages(rabbitJbatisProperties.getMapperPackages());
        Map<String, DataSourceBean> dataSourceMap = new HashMap<>();
        DataSourceBeanProperties dataSourceBeans = rabbitJbatisProperties.getDataSourceBeans();
        String routing = dataSourceBeans.getRoutingDatasourceClass();
        String defaultDataSource = dataSourceBeans.getDefaultDataSource();
        DataSource dataSource = (DataSource) applicationContext.getBean(defaultDataSource);
        Map<String, String> dataSourceName = dataSourceBeans.getDataSourceName();
        String dialect = rabbitJbatisProperties.getDataSources().get(defaultDataSource).getDialect();
        if (StringUtils.isBlank(routing)) {
            DataSourceBean dataSourceBean = new DataSourceBean();
            dataSourceBean.setDataSource(dataSource);
            dataSourceBean.setDialect(dialect);
            dataSourceMap.put("default", dataSourceBean);
            for (Map.Entry<String, String> entry : dataSourceName.entrySet()) {
                String key = entry.getKey();
                String beanName = entry.getValue();
                DataSource dataSource1 = (DataSource) applicationContext.getBean(beanName);
                DataSourceBean dataSourceBean1 = new DataSourceBean();
                dataSourceBean1.setDataSource(dataSource1);
                dataSourceBean1.setDialect(dialect);
                dataSourceMap.put(key, dataSourceBean1);
            }
        } else {
            Class<?> dataSourceClazz = ClassUtils.classForName(routing);
            String key = StringUtils.lowercaseFirstChar(dataSourceClazz.getSimpleName());
            Map<Object, Object> targetDataSources = new HashMap<>();
            for (Map.Entry<String, String> entry : dataSourceName.entrySet()) {
                String keyName = entry.getKey();
                String beanName = entry.getValue();
                DataSource dataSource1 = (DataSource) applicationContext.getBean(beanName);
                targetDataSources.put(keyName, dataSource1);
            }
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClazz);
            beanDefinitionBuilder.addPropertyValue("defaultTargetDataSource", dataSource);
            beanDefinitionBuilder.addPropertyValue("targetDataSources", targetDataSources);
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
            registry.registerBeanDefinition(key, beanDefinition);
            DataSource dataSource1 = (DataSource) applicationContext.getBean(key);
            DataSourceBean dataSourceBean = new DataSourceBean();
            dataSourceBean.setDataSource(dataSource1);
            dataSourceBean.setDialect(dialect);
            dataSourceMap.put("default", dataSourceBean);
        }

        rabbitJbatisFactoryBean.setDataSourceMap(dataSourceMap);
        return rabbitJbatisFactoryBean.getObject();
    }

    private void registerBean(ConfigurableApplicationContext applicationContext) {
        Map<String, DataSourceProperties> dataSources = rabbitJbatisProperties.getDataSources();
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
            registry.registerBeanDefinition(key, beanDefinition);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}