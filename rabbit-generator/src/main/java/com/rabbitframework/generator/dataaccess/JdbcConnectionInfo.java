package com.rabbitframework.generator.dataaccess;

import java.util.Properties;

public class JdbcConnectionInfo {
    private String driverClass;
    private String catalog;
    private String connectionURL;
    private String userName;
    private String password;
    private Properties properties;

    public JdbcConnectionInfo() {
        properties = new Properties();
    }

    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    public Properties getProperties() {
        return properties;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
