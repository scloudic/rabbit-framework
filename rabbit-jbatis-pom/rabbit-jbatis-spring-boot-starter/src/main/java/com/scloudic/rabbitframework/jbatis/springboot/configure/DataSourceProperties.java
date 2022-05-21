package com.scloudic.rabbitframework.jbatis.springboot.configure;

public class DataSourceProperties {
    private String beanName;
    private String dialect = "mysql";

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
