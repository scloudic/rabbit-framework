package com.scloudic.rabbitframework.jbatis.scripting;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.scripting.xmltags.SqlNode;

import java.util.Map;

public class DynamicNodeSqlSource implements SqlSource {
    private Configuration configuration;
    private SqlNode rootSqlNode;

    public DynamicNodeSqlSource(Configuration configuration, SqlNode rootSqlNode) {
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds, String dynamicSQL) {
        DynamicContext context = new DynamicContext(configuration,
                parameterObject);
        if (rowBounds != null) {
            context.bind(Dialect.OFFSET, rowBounds.getOffset());
            context.bind(Dialect.LIMIT, rowBounds.getLimit());
        }
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceBuilder = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class
                : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceBuilder.parse(context.getSql(),
                parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject, rowBounds, dynamicSQL);
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }
}
