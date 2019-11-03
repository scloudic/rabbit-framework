package com.rabbitframework.jade.scripting;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.mapping.BoundSql;
import com.rabbitframework.jade.mapping.ParameterMapping;
import com.rabbitframework.jade.mapping.RowBounds;

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
