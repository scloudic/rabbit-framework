package com.scloudic.rabbitframework.jbatis.dataaccess;

import javax.sql.DataSource;

public class DataSourceBean {
	private String dialect;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getDialect() {
		return dialect;
	}
}
