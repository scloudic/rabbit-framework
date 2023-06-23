package com.scloudic.rabbitframework.jbatis.scripting;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.scripting.xmltags.XMLScriptBuilder;

public class DynamicSqlSource implements SqlSource {
    private Configuration configuration;
    private String rootSql;

    public DynamicSqlSource(Configuration configuration, String rootSql) {
        this.configuration = configuration;
        this.rootSql = rootSql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds,
                                String dynamicSQL) {
        String sql = String.format(rootSql, dynamicSQL);
        XMLScriptBuilder scriptBuilder = new XMLScriptBuilder(configuration, sql);
        SqlSource sqlSource = scriptBuilder.parse();
        return sqlSource.getBoundSql(parameterObject, rowBounds, null);
    }
}
