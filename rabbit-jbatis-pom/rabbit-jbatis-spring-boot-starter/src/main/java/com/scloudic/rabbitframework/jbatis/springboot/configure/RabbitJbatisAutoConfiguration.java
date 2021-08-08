package com.scloudic.rabbitframework.jbatis.springboot.configure;

import com.scloudic.rabbitframework.core.utils.ClassUtils;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
		registerTransactionManagement((ConfigurableApplicationContext) applicationContext);
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
		RabbitJbatisFactoryBean rabbitJbatisFactoryBean = new RabbitJbatisFactoryBean();
		rabbitJbatisFactoryBean.setDataSourceFactory(dataSourceFactory());
		rabbitJbatisFactoryBean.setEntityPackages(rabbitJbatisProperties.getEntityPackages());
		rabbitJbatisFactoryBean.setMapperPackages(rabbitJbatisProperties.getMapperPackages());
		Map<String, DataSourceBean> dataSourceMap = new HashMap<>();
		Map<String, String> dataSourceBeans = rabbitJbatisProperties.getDataSourceBeans();
		dataSourceBeans.forEach((k, v) -> {
			DataSource dataSource = (DataSource) applicationContext.getBean(v);
			String dialect = rabbitJbatisProperties.getDataSources().get(v).getDialect();
			DataSourceBean dataSourceBean = new DataSourceBean();
			dataSourceBean.setDataSource(dataSource);
			dataSourceBean.setDialect(dialect);
			dataSourceMap.put(k, dataSourceBean);
		});
		rabbitJbatisFactoryBean.setDataSourceMap(dataSourceMap);
		return rabbitJbatisFactoryBean.getObject();
	}

	private void registerBean(ConfigurableApplicationContext applicationContext) {
		Map<String, RabbitJbatisProperties.DataSourceProperties> dataSources = rabbitJbatisProperties.getDataSources();
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
		Map<String, String> dataSourceBeans = rabbitJbatisProperties.getDataSourceBeans();
		Map<String, RabbitJbatisProperties.DataSourceProperties> dataSources = rabbitJbatisProperties.getDataSources();
		dataSourceBeans.forEach((k, v) -> {
			RabbitJbatisProperties.DataSourceProperties dataSourceProperties = dataSources.get(v);
			// 数据库开启事务
			if (dataSourceProperties.isTransaction()) {
				DataSource dataSource = (DataSource) applicationContext.getBean(v);
				BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
						.genericBeanDefinition(DataSourceTransactionManager.class);
				beanDefinitionBuilder.addPropertyValue("dataSource", dataSource);
				BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
				registry.registerBeanDefinition(v + "Tx", beanDefinition);
			}
		});
	}
}