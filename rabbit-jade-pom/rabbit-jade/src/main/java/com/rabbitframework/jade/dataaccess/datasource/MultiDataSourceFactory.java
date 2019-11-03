package com.rabbitframework.jade.dataaccess.datasource;

import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.rabbitframework.jade.annontations.Mapper;
import com.rabbitframework.jade.dataaccess.DataSourceBean;
import com.rabbitframework.jade.dataaccess.dialect.DefaultDialect;
import com.rabbitframework.jade.dataaccess.dialect.Dialect;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.tjzq.commons.utils.StringUtils;

/**
 * 多数据源配置,根据{@link Mapper}中的 catalog中的名称来确定
 * 如果名称为空将使用默认的DataSource数据源,但是需要配置默认数据源，只要key为"default"时，将使用默认数据源,
 */
public class MultiDataSourceFactory implements DataSourceFactory {
	private final static String DEFAULT = "default";
	private ConcurrentHashMap<String, DataSourceBean> dataSources = new ConcurrentHashMap<String, DataSourceBean>();
	private DataSourceBean defaultDataSource;

	@Override
	public void addDataSource(String key, DataSourceBean dataSourceBean) {
		if (StringUtils.isEmpty(key) || dataSourceBean == null) {
			throw new NullPointerException("DataSource add fail: key=" + key + ",dataSourceBean=" + dataSourceBean);
		}
		if (DEFAULT.equals(key)) {
			defaultDataSource = dataSourceBean;
		} else {
			dataSources.putIfAbsent(key, dataSourceBean);
		}
	}

	@Override
	public DataSource getDataSource(MappedStatement mappedStatement) {
		String catalog = mappedStatement.getCatalog();
		DataSourceBean dataSource = dataSources.get(catalog);
		if (dataSource != null) {
			return dataSource.getDataSource();
		}
		if (StringUtils.isBlank(catalog)) {
			return defaultDataSource.getDataSource();
		}
		return null;
	}

	@Override
	public Dialect getDialect(MappedStatement mappedStatement) {
		String catalog = mappedStatement.getCatalog();
		DataSourceBean dataSource = dataSources.get(catalog);
		if (dataSource != null) {
			String dialectStr = dataSource.getDialect();
			return DefaultDialect.newInstances(dialectStr);
		}
		if (StringUtils.isBlank(catalog)) {
			String dialectStr = defaultDataSource.getDialect();
			return DefaultDialect.newInstances(dialectStr);
		}
		return null;
	}
}
