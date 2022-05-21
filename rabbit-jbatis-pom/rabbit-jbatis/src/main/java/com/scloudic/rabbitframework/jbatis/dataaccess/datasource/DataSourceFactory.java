package com.scloudic.rabbitframework.jbatis.dataaccess.datasource;

import javax.sql.DataSource;

import com.scloudic.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.scloudic.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;

/**
 * 数据源工厂类
 */
public interface DataSourceFactory {
	void addDataSource(String key, DataSourceBean dataSourceBean);

	DataSource getDataSource(MappedStatement mappedStatement);
	
	Dialect getDialect(MappedStatement mappedStatement);
}
