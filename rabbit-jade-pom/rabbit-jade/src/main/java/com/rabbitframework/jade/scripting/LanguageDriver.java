package com.rabbitframework.jade.scripting;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.executor.ParameterHandler;
import com.rabbitframework.jade.mapping.BoundSql;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.executor.DefaultParameterHandler;


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
