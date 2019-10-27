package com.rabbitframework.dbase.spring;

import static org.springframework.util.Assert.notNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import com.rabbitframework.dbase.RabbitDbaseFactory;
import com.rabbitframework.dbase.RabbitDbaseFactoryBuilder;
import com.rabbitframework.dbase.builder.Configuration;
import com.rabbitframework.dbase.builder.XMLConfigBuilder;
import com.rabbitframework.dbase.cache.Cache;
import com.rabbitframework.dbase.dataaccess.DataSourceBean;
import com.rabbitframework.dbase.dataaccess.Environment;
import com.rabbitframework.dbase.dataaccess.datasource.DataSourceFactory;
import com.tjzq.commons.utils.StringUtils;

public class RabbitDbaseFactoryBean
		implements FactoryBean<RabbitDbaseFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
	private static final Logger logger = LoggerFactory.getLogger(RabbitDbaseFactoryBean.class);
	private RabbitDbaseFactoryBuilder rabbitDbaseFactoryBuilder = new RabbitDbaseFactoryBuilder();
	private RabbitDbaseFactory rabbitDbaseFactory;
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

	public void setRabbitDbaseFactoryBuilder(RabbitDbaseFactoryBuilder rabbitDbaseFactoryBuilder) {
		this.rabbitDbaseFactoryBuilder = rabbitDbaseFactoryBuilder;
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
	public RabbitDbaseFactory getObject() throws Exception {
		if (this.rabbitDbaseFactory == null) {
			afterPropertiesSet();
		}
		return this.rabbitDbaseFactory;
	}

	@Override
	public Class<? extends RabbitDbaseFactory> getObjectType() {
		return this.rabbitDbaseFactory == null ? RabbitDbaseFactory.class : this.rabbitDbaseFactory.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(dataSourceMap, "Property 'dataSourceMap' is required");
		notNull(rabbitDbaseFactoryBuilder, "Property 'RabbitDbaseFactoryBuilder' is required");
		notNull(dataSourceFactory, "Property 'dataSourceFactory' is required");
		this.rabbitDbaseFactory = buildRabbitDbaseFactory();
	}

	public RabbitDbaseFactory buildRabbitDbaseFactory() throws Exception {
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
		return rabbitDbaseFactoryBuilder.build(configuration);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (failFast && event instanceof ContextRefreshedEvent) {
			this.rabbitDbaseFactory.getConfiguration().getMappedStatementNames();
		}
	}
}
