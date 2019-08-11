package com.rabbitframework.dbase.scripting;

import com.rabbitframework.dbase.builder.Configuration;
import com.rabbitframework.dbase.executor.ParameterHandler;
import com.rabbitframework.dbase.mapping.BoundSql;
import com.rabbitframework.dbase.mapping.MappedStatement;


public interface LanguageDriver {
    /**
     * 创建参数处理类{@link com.rabbitframework.dbase.executor.DefaultParameterHandler}
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
