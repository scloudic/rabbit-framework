package com.rabbitframework.dbase.scripting;

import com.rabbitframework.dbase.builder.Configuration;
import com.rabbitframework.dbase.executor.DefaultParameterHandler;
import com.rabbitframework.dbase.executor.ParameterHandler;
import com.rabbitframework.dbase.mapping.BoundSql;
import com.rabbitframework.dbase.mapping.MappedStatement;
import com.rabbitframework.dbase.scripting.xmltags.XMLScriptBuilder;

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
