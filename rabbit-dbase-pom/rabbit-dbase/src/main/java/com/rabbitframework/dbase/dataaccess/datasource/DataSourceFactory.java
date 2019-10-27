package com.rabbitframework.dbase.dataaccess.datasource;

import javax.sql.DataSource;

import com.rabbitframework.dbase.dataaccess.DataSourceBean;
import com.rabbitframework.dbase.dataaccess.dialect.Dialect;
import com.rabbitframework.dbase.mapping.MappedStatement;

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
