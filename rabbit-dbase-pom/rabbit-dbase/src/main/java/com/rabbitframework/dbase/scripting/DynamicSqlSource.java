package com.rabbitframework.dbase.scripting;

import com.rabbitframework.dbase.builder.Configuration;
import com.rabbitframework.dbase.dataaccess.dialect.Dialect;
import com.rabbitframework.dbase.mapping.BoundSql;
import com.rabbitframework.dbase.mapping.RowBounds;
import com.rabbitframework.dbase.scripting.xmltags.SqlNode;

import java.util.Map;

public class DynamicSqlSource implements SqlSource {
	private Configuration configuration;
	private SqlNode rootSqlNode;

	public DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
		this.configuration = configuration;
		this.rootSqlNode = rootSqlNode;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds) {
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
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject, rowBounds);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
			boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}
		return boundSql;
	}
}
