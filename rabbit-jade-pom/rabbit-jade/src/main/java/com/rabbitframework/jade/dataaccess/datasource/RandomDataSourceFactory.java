package com.rabbitframework.jade.dataaccess.datasource;

import com.rabbitframework.jade.dataaccess.DataSourceBean;
import com.rabbitframework.jade.dataaccess.dialect.DefaultDialect;
import com.rabbitframework.jade.dataaccess.dialect.Dialect;
import com.rabbitframework.jade.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

public class RandomDataSourceFactory implements DataSourceFactory {
	private Random random = new Random();
	private List<DataSourceBean> dataSources = Collections.emptyList();

	@Override
	public void addDataSource(String key, DataSourceBean dataSourceBean) {
		if (dataSourceBean == null) {
			throw new NullPointerException("dataSourceBean is null");
		}
		if (dataSources.size() == 0) {
			dataSources = new ArrayList<DataSourceBean>(dataSources);
		}
	}

	@Override
	public DataSource getDataSource(MappedStatement mappedStatement) {
		if (dataSources.size() == 0) {
			return null;
		}
		int index = random.nextInt(dataSources.size()); // 0.. size
		return dataSources.get(index).getDataSource();
	}

	@Override
	public Dialect getDialect(MappedStatement mappedStatement) {
		if (dataSources.size() == 0) {
			return null;
		}
		int index = random.nextInt(dataSources.size()); // 0.. size
		String dialectStr = dataSources.get(index).getDialect();
		return DefaultDialect.newInstances(dialectStr);
	}
}
