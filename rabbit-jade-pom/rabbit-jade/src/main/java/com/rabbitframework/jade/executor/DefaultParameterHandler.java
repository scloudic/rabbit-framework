package com.rabbitframework.jade.executor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

import com.rabbitframework.jade.reflect.MetaObject;
import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.mapping.BoundSql;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.mapping.ParameterMapping;

public class DefaultParameterHandler implements ParameterHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultParameterHandler.class);
    private Configuration configuration;
    private final Object parameterObject;
    private BoundSql boundSql;

    public DefaultParameterHandler(MappedStatement mappedStatement,
                                   Object parameterObject, BoundSql boundSql) {
        this.parameterObject = parameterObject;
        configuration = mappedStatement.getConfiguration();
        this.boundSql = boundSql;
    }

    @SuppressWarnings("unchecked")
    public void setParameters(PreparedStatement ps) throws SQLException {
        try {
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            if (parameterMappings != null) {
                int paramMappingSize = parameterMappings.size();
                for (int i = 0; i < paramMappingSize; i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = "";
                    } else {
                        Class<?> type = parameterObject.getClass();
                        boolean isPrimitive = isColumnType(type);
                        if (isPrimitive) {
                            value = parameterObject;
                        } else {
                            MetaObject metaObject = configuration.newMetaObject(parameterObject);
                            value = metaObject.getValue(propertyName);
                        }
                    }
                    StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, value);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SQLException(e.getMessage(), e);
        }
    }

    private boolean isColumnType(Class<?> columnTypeCandidate) {
        return String.class == columnTypeCandidate
                || org.springframework.util.ClassUtils
                .isPrimitiveOrWrapper(columnTypeCandidate);
    }
}