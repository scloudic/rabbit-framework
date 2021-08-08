package com.scloudic.rabbitframework.jbatis.dataaccess.datasource;

import javax.sql.DataSource;

import com.scloudic.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.scloudic.rabbitframework.core.utils.StringUtils;

/**
 * 主从数据源配置,所有写操作使用master,读操作使用slave 允许有多master/salve,使用
 * {@link RandomDataSourceFactory}随机获取{@link DataSourceFactory}
 * 在多master/salve数据源情况下，主要通过key来区分,key中只要包含有master/salve字符,程序将自动匹配
 * 当不匹配master/savle时，将默认同时都可以使用二者
 */
public class MasterSlaveDataSourceFactory implements DataSourceFactory {
	private static final Logger logger = LoggerFactory.getLogger(MasterSlaveDataSourceFactory.class);
	private final static String MASTER = "master";
	private final static String SLAVE = "slave";
	private DataSourceFactory masters = new RandomDataSourceFactory();
	private DataSourceFactory slaves = new RandomDataSourceFactory();

	@Override
	public void addDataSource(String key, DataSourceBean dataSourceBean) {
		if (StringUtils.isEmpty(key) || dataSourceBean == null) {
			throw new NullPointerException("DataSource add fail: key=" + key + ",DataDataSource=" + dataSourceBean);
		}
		String name = key.toLowerCase();
		if (name.contains(MASTER)) {
			masters.addDataSource(key, dataSourceBean);
		} else if (name.contains(SLAVE)) {
			slaves.addDataSource(key, dataSourceBean);
		} else {
			int index = name.lastIndexOf(MASTER);
			int index1 = name.lastIndexOf(SLAVE);
			if (index == -1 && index1 == -1) {
				slaves.addDataSource(key, dataSourceBean);
				masters.addDataSource(key, dataSourceBean);
			}
		}
	}

	@Override
	public DataSource getDataSource(MappedStatement mappedStatement) {
		SqlCommendType sqlCommendType = mappedStatement.getSqlCommendType();
		if (sqlCommendType == SqlCommendType.SELECT) {
			logger.debug("into slaves");
			return slaves.getDataSource(mappedStatement);
		} else {
			logger.debug("into master");
			return masters.getDataSource(mappedStatement);
		}
	}

	@Override
	public Dialect getDialect(MappedStatement mappedStatement) {
		SqlCommendType sqlCommendType = mappedStatement.getSqlCommendType();
		if (sqlCommendType == SqlCommendType.SELECT) {
			return slaves.getDialect(mappedStatement);
		} else {
			return masters.getDialect(mappedStatement);
		}
	}

}
