package com.rabbitframework.jbatis.executor;

import java.util.List;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.jbatis.mapping.BoundSql;
import com.rabbitframework.jbatis.mapping.MappedStatement;
import com.rabbitframework.jbatis.mapping.RowBounds;

/**
 * 执行
 */
public class SimpleExecutor implements Executor {
	private Configuration configuration;
	private JdbcTemplateHolder jdbcTemplateHolder;

	public SimpleExecutor(Configuration configuration, JdbcTemplateHolder jdbcTemplateHolder) {
		this.configuration = configuration;
		this.jdbcTemplateHolder = jdbcTemplateHolder;
	}

	@Override
	public int update(MappedStatement ms, Object parameter) {
		BoundSql boundSql = ms.getBoundSql(parameter, null);
		StatementHandler statementHandler = configuration.newStatementHandler(ms, new Object[] { parameter }, boundSql);
		return statementHandler.update(jdbcTemplateHolder);
	}

	@Override
	public int batchUpdate(MappedStatement ms, List<Object> parameter) {
		int parameterSize = parameter.size();
		BoundSql[] boundSqls = new BoundSql[parameterSize];
		Object[] parameterObj = new Object[parameterSize];
		for (int i = 0; i < parameterSize; i++) {
			Object object = parameter.get(i);
			BoundSql boundSql = ms.getBoundSql(object, null);
			boundSqls[i] = boundSql;
			parameterObj[i] = object;
		}
		StatementHandler statementHandler = configuration.newStatementHandler(ms, parameterObj, boundSqls);
		return statementHandler.batchUpdate(jdbcTemplateHolder);
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) {
		BoundSql boundSql = ms.getBoundSql(parameter, rowBounds);
		StatementHandler statementHandler = configuration.newStatementHandler(ms, new Object[] { parameter }, boundSql);
		return statementHandler.query(jdbcTemplateHolder);
	}

}
