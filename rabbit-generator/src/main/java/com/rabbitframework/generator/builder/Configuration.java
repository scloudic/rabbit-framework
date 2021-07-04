package com.rabbitframework.generator.builder;

import java.util.Properties;

import com.rabbitframework.generator.dataaccess.JdbcConnectionInfo;
import com.rabbitframework.generator.mapping.type.JavaTypeResolver;
import com.rabbitframework.generator.mapping.type.JavaTypeResolverDefaultImpl;
import com.rabbitframework.generator.template.Template;

public class Configuration {
    private Properties variables;
    private JdbcConnectionInfo jdbcConnectionInfo;
    private Template template;
    private TableConfiguration tableConfiguration;
    private JavaTypeResolver javaTypeResolver;

    public Configuration() {
        javaTypeResolver = new JavaTypeResolverDefaultImpl();
    }

    public Properties getVariables() {
        return variables;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public void setJdbcConnectionInfo(JdbcConnectionInfo jdbcConnectionInfo) {
        this.jdbcConnectionInfo = jdbcConnectionInfo;
    }

    public JdbcConnectionInfo getJdbcConnectionInfo() {
        return jdbcConnectionInfo;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public JavaTypeResolver getJavaTypeResolver() {
        return javaTypeResolver;
    }
}
