package com.scloudic.rabbitframework.jbatis.scripting;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.executor.ParameterHandler;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.executor.DefaultParameterHandler;


public interface LanguageDriver {
    /**
     * 创建参数处理类{@link DefaultParameterHandler}
     * @param mappedStatement mappedStatement
     * @param parameterObject parameterObject
     * @param boundSql boundSql
     * @return ParameterHandler
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement,
                                            Object parameterObject, BoundSql boundSql);

    /**
     * 创建{@link SqlSource}
     *
     * @param configuration configuration
     * @param sqlScript sqlScript
     * @return SqlSource
     */
    SqlSource createSqlSource(Configuration configuration, String sqlScript);
}
