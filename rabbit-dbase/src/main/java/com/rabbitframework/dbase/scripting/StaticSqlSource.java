package com.rabbitframework.dbase.scripting;

import com.rabbitframework.dbase.builder.Configuration;
import com.rabbitframework.dbase.mapping.BoundSql;
import com.rabbitframework.dbase.mapping.ParameterMapping;
import com.rabbitframework.dbase.mapping.RowBounds;

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
