package com.rabbitframework.jade.scripting;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.executor.DefaultParameterHandler;
import com.rabbitframework.jade.executor.ParameterHandler;
import com.rabbitframework.jade.mapping.BoundSql;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.scripting.xmltags.XMLScriptBuilder;

public class LanguageDriverImpl implements LanguageDriver {
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject,
                boundSql);
    }
    /**
     * 创建{@link SqlSource}数据源
     * <p/>
     * 通过{@link XMLScriptBuilder}创建{@link DynamicSqlSource}
     *
     * @param configuration
     * @param sqlScript
     * @return
     */
    @Override
    public SqlSource createSqlSource(Configuration configuration, String sqlScript) {
        XMLScriptBuilder scriptBuilder = new XMLScriptBuilder(configuration, sqlScript);
        return scriptBuilder.parse();
    }
}
