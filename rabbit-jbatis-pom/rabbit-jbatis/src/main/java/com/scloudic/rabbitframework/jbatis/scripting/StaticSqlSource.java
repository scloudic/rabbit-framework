package com.scloudic.rabbitframework.jbatis.scripting;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.ParameterMapping;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

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
