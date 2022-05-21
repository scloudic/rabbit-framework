package com.scloudic.rabbitframework.jbatis.dataaccess;

import com.scloudic.rabbitframework.jbatis.dataaccess.datasource.DataSourceFactory;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcTemplateHolder {
    private Environment environment;
    private DataSourceFactory dataSourceFactory;

    public JdbcTemplateHolder(Environment environment) {
        this.environment = environment;
        dataSourceFactory = environment.getDataSourceFactory();
    }

    public JdbcTemplate getJdbcTemplate(MappedStatement mappedStatement) {
        DataSource dataSource = dataSourceFactory.getDataSource(mappedStatement);
        if (dataSource == null) {
            throw new NullPointerException("cannot found a dataSource for: " + mappedStatement);
        }
        return environment.getJdbcTemplate(dataSource);
    }
}
