package com.scloudic.rabbitframework.jbatis.springboot.configure;

import java.util.HashMap;
import java.util.Map;

public class DataSourceProperties {
    private String className;
    private String dialect = "mysql";
    private Map<String, Object> params = new HashMap<String, Object>();

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDialect() {
        return dialect;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
