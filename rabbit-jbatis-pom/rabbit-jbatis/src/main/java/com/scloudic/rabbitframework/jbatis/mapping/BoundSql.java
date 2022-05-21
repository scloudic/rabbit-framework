package com.scloudic.rabbitframework.jbatis.mapping;

import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.jbatis.builder.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql {
    private MetaObject metaParameters;
    private String sql;
    private Object parameterObject;
    private List<ParameterMapping> parameterMappings;
    private Map<String, Object> additionalParameters;

    public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        additionalParameters = new HashMap<String, Object>();
        metaParameters = configuration.newMetaObject(additionalParameters);

    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public boolean hasAdditionalParameter(String name) {
        return metaParameters.hasGetter(name);
    }

    public Object getAdditionalParameter(String name) {
        return metaParameters.getValue(name);
    }

    public void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

}
