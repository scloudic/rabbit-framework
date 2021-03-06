package com.rabbitframework.jbatis.scripting;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.executor.ParameterHandler;
import com.rabbitframework.jbatis.mapping.BoundSql;
import com.rabbitframework.jbatis.mapping.MappedStatement;
import com.rabbitframework.jbatis.executor.DefaultParameterHandler;


public interface LanguageDriver {
    /**
     * 创建参数处理类{@link DefaultParameterHandler}
     * @param mappedStatement
     * @param parameterObject
     * @param boundSql
     * @return
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement,
                                            Object parameterObject, BoundSql boundSql);

    /**
     * 创建{@link SqlSource}
     *
     * @param configuration
     * @param sqlScript
     * @return
     */
    SqlSource createSqlSource(Configuration configuration, String sqlScript);
}
