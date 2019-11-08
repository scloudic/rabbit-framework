package com.rabbitframework.jbatis.dataaccess;


import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.rabbitframework.jbatis.dataaccess.datasource.DataSourceFactory;

public class Environment {
    private ConcurrentHashMap<DataSource, JdbcTemplate> cacheDataSource = new ConcurrentHashMap<DataSource, JdbcTemplate>();
    private DataSourceFactory dataSourceFactory;

    public void addCacheDataSource(DataSource dataSource) {
        if (cacheDataSource.get(dataSource) == null) {
            cacheDataSource.putIfAbsent(dataSource, new JdbcTemplate(dataSource));
        }
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return cacheDataSource.get(dataSource);
    }
}
