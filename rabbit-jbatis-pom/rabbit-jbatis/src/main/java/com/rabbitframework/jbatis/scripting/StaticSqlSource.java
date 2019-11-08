package com.rabbitframework.jbatis.scripting;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.mapping.BoundSql;
import com.rabbitframework.jbatis.mapping.ParameterMapping;
import com.rabbitframework.jbatis.mapping.RowBounds;

import java.util.List;

public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;


    public StaticSqlSource(Configuration configuration, String sql,
                           List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    public BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds) {
        return new BoundSql(configuration, sql, parameterMappings,
                parameterObject);
    }

}
