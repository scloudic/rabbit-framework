package com.rabbitframework.jbatis.dataaccess.datasource;

import javax.sql.DataSource;

import com.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.rabbitframework.jbatis.dataaccess.dialect.DefaultDialect;
import com.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.rabbitframework.jbatis.mapping.MappedStatement;

/**
 * 简单数据源工厂，此类只能添加一个{@link DataSource} 数据源
 */
public class SimpleDataSourceFactory implements DataSourceFactory {
	private DataSourceBean dataSource;

	@Override
	public void addDataSource(String key, DataSourceBean dataSource) {
		if (dataSource == null) {
			throw new NullPointerException("dataSource is null");
		}
		this.dataSource = dataSource;
	}

	@Override
	public DataSource getDataSource(MappedStatement mappedStatement) {
		return dataSource.getDataSource();
	}

	@Override
	public Dialect getDialect(MappedStatement mappedStatement) {
		String dialectStr = dataSource.getDialect();
		return DefaultDialect.newInstances(dialectStr);
	}

}
