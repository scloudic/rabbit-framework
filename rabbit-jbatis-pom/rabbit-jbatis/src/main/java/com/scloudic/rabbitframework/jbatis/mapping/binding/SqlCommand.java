package com.scloudic.rabbitframework.jbatis.mapping.binding;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.exceptions.BindingException;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

import java.lang.reflect.Method;

public class SqlCommand {
    private String name;
    private SqlCommendType commendType;
    private boolean batchUpdate = false;

    public SqlCommand(Class<?> mapperInterface, Method method, Configuration configuration) {
        String statementName = mapperInterface.getName() + "."
                + method.getName();
        MappedStatement ms = null;
        if (configuration.hasStatement(statementName)) {
            ms = configuration.getMappedStatement(statementName);
        } else if (!mapperInterface.equals(method.getDeclaringClass()
                .getName())) {
            String parentStatementName = method.getDeclaringClass()
                    .getName() + "." + method.getName();
            if (configuration.hasStatement(parentStatementName)) {
                ms = configuration.getMappedStatement(parentStatementName);
            }
        }
        if (ms == null) {
            throw new BindingException(
                    "Invalid bound statement (not found): " + statementName);
        }
        name = ms.getId();
        commendType = ms.getSqlCommendType();
        batchUpdate = ms.isBatchUpdate();
        if (commendType == SqlCommendType.UNKNOWN) {
            throw new BindingException("Unknown execution method for: "
                    + name);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isBatchUpdate() {
        return batchUpdate;
    }

    public SqlCommendType getCommendType() {
        return commendType;
    }
}