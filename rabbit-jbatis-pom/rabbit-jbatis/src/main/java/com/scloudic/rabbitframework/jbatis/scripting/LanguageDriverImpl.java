package com.scloudic.rabbitframework.jbatis.scripting;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.executor.DefaultParameterHandler;
import com.scloudic.rabbitframework.jbatis.executor.ParameterHandler;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.scripting.sql.BaseSQLParser;
import com.scloudic.rabbitframework.jbatis.scripting.xmltags.XMLScriptBuilder;

public class LanguageDriverImpl implements LanguageDriver {
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject,
                boundSql);
    }

    /**
     * 创建{@link SqlSource}数据源
     * <p>
     * 通过{@link XMLScriptBuilder}创建{@link DynamicNodeSqlSource}
     *
     * @param configuration configuration
     * @param sqlScript     sqlScript
     * @return SqlSource
     */
    @Override
    public SqlSource createSqlSource(Configuration configuration, String sqlScript,
                                     BaseSQLParser sqlParser) {
        if (sqlParser.isDynamicSql()) {
            DynamicSqlSource dynamicSqlSource =
                    new DynamicSqlSource(configuration, sqlScript);
            return dynamicSqlSource;
        }
        XMLScriptBuilder scriptBuilder = new XMLScriptBuilder(configuration, sqlScript);
        return scriptBuilder.parse();
    }
}
