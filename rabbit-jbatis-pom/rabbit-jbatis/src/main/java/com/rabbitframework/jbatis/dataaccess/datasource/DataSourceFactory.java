package com.rabbitframework.jbatis.dataaccess.datasource;

import javax.sql.DataSource;

import com.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.rabbitframework.jbatis.mapping.MappedStatement;

/**
 * 数据源工厂类
 */
public interface DataSourceFactory {
	/**
	 * 添加数据源
	 *
	 * @param key
	 * @param dataSourceBean
	 */
	void addDataSource(String key, DataSourceBean dataSourceBean);

	DataSource getDataSource(MappedStatement mappedStatement);
	
	Dialect getDialect(MappedStatement mappedStatement);
}
