package com.rabbitframework.jbatis.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rabbitframework.jbatis.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.jbatis.dataaccess.KeyGenerator;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;

import com.rabbitframework.jbatis.log.ConnectionLogger;
import com.rabbitframework.jbatis.mapping.BoundSql;
import com.rabbitframework.jbatis.mapping.GenerationType;
import com.rabbitframework.jbatis.mapping.MappedStatement;
import com.rabbitframework.jbatis.mapping.SqlCommendType;
import com.rabbitframework.jbatis.reflect.MetaObject;

public class PreparedStatementHandler implements StatementHandler {
	private MappedStatement mappedStatement;
	private Object[] parameterObject;
	private BoundSql[] boundSql;

	public PreparedStatementHandler(MappedStatement mappedStatement, Object[] parameterObject, BoundSql... boundSql) {
		this.mappedStatement = mappedStatement;
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	@Override
	public BoundSql[] getBoundSql() {
		return boundSql;
	}

	@Override
	public int update(JdbcTemplateHolder jdbcTemplateHolder) {
		KeyHolder generatedKeyHolder = null;
		int result;
		List<KeyGenerator> keyGenerators = mappedStatement.getKeyGenerators();
		int keyGeneratorSize = keyGenerators.size();
		if (keyGeneratorSize > 0 && mappedStatement.getSqlCommendType() == SqlCommendType.INSERT) {
			generatedKeyHolder = new GeneratedKeyHolder();
		}
		JdbcTemplate jdbcTemplate = jdbcTemplateHolder.getJdbcTemplate(mappedStatement);

		PreparedStatementCreator preparedStatement = createPreparedStatement();
		if (generatedKeyHolder == null) {
			result = jdbcTemplate.update(preparedStatement);
		} else {
			result = jdbcTemplate.update(preparedStatement, generatedKeyHolder);
			Number number = generatedKeyHolder.getKey();
			MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(parameterObject[0]);
			for (int i = 0; i < keyGeneratorSize; i++) {
				KeyGenerator keyGenerator = keyGenerators.get(i);
				String property = keyGenerator.getProperty();
				if (metaObject.hasSetter(property)) {
					Object key = getKeyValue(number, keyGenerator.getJavaType());
					if (key != null) {
						metaObject.setValue(property, key);
					}
				}
			}
		}
		return result;
	}

	private Object getKeyValue(Number result, Class<?> returnType) {
		if (returnType.isPrimitive()) {
			returnType = ClassUtils.primitiveToWrapper(returnType);
		}
		if (result == null || returnType == void.class) {
			return null;
		}
		if (returnType == result.getClass()) {
			return result;
		}
		// 将结果转成方法的返回类型
		if (returnType == Integer.class) {
			return result.intValue();
		} else if (returnType == Long.class) {
			return result.longValue();
		} else if (returnType == Boolean.class) {
			return result.intValue() > 0 ? Boolean.TRUE : Boolean.FALSE;
		} else if (returnType == Double.class) {
			return result.doubleValue();
		} else if (returnType == Float.class) {
			return result.floatValue();
		} else if (returnType == Number.class) {
			return result;
		} else if (returnType == String.class || returnType == CharSequence.class) {
			return String.valueOf(result);
		} else {
			return null;
		}
	}

	@Override
	public <E> List<E> query(JdbcTemplateHolder jdbcTemplateHolder) {
		JdbcTemplate jdbcTemplate = jdbcTemplateHolder.getJdbcTemplate(mappedStatement);
		PreparedStatementCreator preparedStatement = createPreparedStatement();
		RowMapper<E> rowMapper = mappedStatement.getRowMapper();
		return jdbcTemplate.query(preparedStatement, new RowMapperResultSetExtractor<E>(rowMapper));
	}

	@Override
	public PreparedStatementCreator createPreparedStatement() {
		boolean isIdentity = false;
		String[] columnNames = null;
		if (SqlCommendType.INSERT == mappedStatement.getSqlCommendType()) {
			List<KeyGenerator> keyGenerators = mappedStatement.getKeyGenerators();
			columnNames = new String[keyGenerators.size()];
			int i = 0;
			for (KeyGenerator keyGenerator : keyGenerators) {
				if (keyGenerator.isIdentity()) {
					isIdentity = true;
					break;
				} else {
					if (GenerationType.SEQUENCE == keyGenerator.getGenerationType()) {
						columnNames[i] = keyGenerator.getColumn();
						i++;
					}
				}
			}
		}
		return getPreparedStatementCreator(isIdentity, columnNames);
	}

	private PreparedStatementCreator getPreparedStatementCreator(final boolean isIdentity, final String[] keyColumn) {
		final Logger logger = mappedStatement.getStatementLogger();
		final String sql = getBoundSql()[0].getSql();
		final ParameterHandler parameterHandler = mappedStatement.getConfiguration()
				.newParameterHandler(mappedStatement, parameterObject[0], boundSql[0]);
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				if (logger.isDebugEnabled()) {
					con = ConnectionLogger.newInstance(con, logger);
				}
				PreparedStatement ps;
				if (isIdentity) {
					ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				} else if (keyColumn != null && keyColumn.length > 0) {
					ps = con.prepareStatement(sql, keyColumn);
				} else {
					ps = con.prepareStatement(sql);
				}
				parameterHandler.setParameters(ps);
				return ps;
			}
		};
		return creator;
	}

	@Override
	public int batchUpdate(JdbcTemplateHolder jdbcTemplateHolder) {
		JdbcTemplate jdbcTemplate = jdbcTemplateHolder.getJdbcTemplate(mappedStatement);
		String sql = boundSql[0].getSql();
		int[] results = batchUpdate(sql, jdbcTemplate,
				new SimpleBatchPreparedStatementSetter(mappedStatement, parameterObject, boundSql));
		return results.length;
	}

	private int[] batchUpdate(String sql, JdbcTemplate jdbcTemplate, final BatchPreparedStatementSetter pss)
			throws DataAccessException {
		final Logger logger = mappedStatement.getStatementLogger();
		return jdbcTemplate.execute(new SimplePreparedStatementCreator(sql, logger),
				new PreparedStatementCallback<int[]>() {
					@Override
					public int[] doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
						try {
							int batchSize = pss.getBatchSize();
							InterruptibleBatchPreparedStatementSetter ipss = (pss instanceof InterruptibleBatchPreparedStatementSetter
									? (InterruptibleBatchPreparedStatementSetter) pss : null);
							if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
								for (int i = 0; i < batchSize; i++) {
									pss.setValues(ps, i);
									if (ipss != null && ipss.isBatchExhausted(i)) {
										break;
									}
									ps.addBatch();
								}
								return ps.executeBatch();
							} else {
								List<Integer> rowsAffected = new ArrayList<Integer>();
								for (int i = 0; i < batchSize; i++) {
									pss.setValues(ps, i);
									if (ipss != null && ipss.isBatchExhausted(i)) {
										break;
									}
									rowsAffected.add(ps.executeUpdate());
								}
								int[] rowsAffectedArray = new int[rowsAffected.size()];
								for (int i = 0; i < rowsAffectedArray.length; i++) {
									rowsAffectedArray[i] = rowsAffected.get(i);
								}
								return rowsAffectedArray;
							}
						} finally {
							if (pss instanceof ParameterDisposer) {
								((ParameterDisposer) pss).cleanupParameters();
							}
						}
					}
				});
	}

	private static class SimpleBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
		private final Object[] parameterObject;
		private final BoundSql[] boundSql;
		private final MappedStatement mappedStatement;

		public SimpleBatchPreparedStatementSetter(MappedStatement mappedStatement, Object[] parameterObject,
				BoundSql[] boundSql) {
			this.parameterObject = parameterObject;
			this.boundSql = boundSql;
			this.mappedStatement = mappedStatement;
		}

		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			final ParameterHandler parameterHandler = mappedStatement.getConfiguration()
					.newParameterHandler(mappedStatement, parameterObject[i], boundSql[i]);
			parameterHandler.setParameters(ps);
		}

		@Override
		public int getBatchSize() {
			return boundSql.length;
		}
	}

	/**
	 * Simple adapter for PreparedStatementCreator, allowing to use a plain SQL
	 * statement.
	 */
	private static class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

		private final String sql;
		private final Logger logger;

		public SimplePreparedStatementCreator(String sql, Logger logger) {
			this.sql = sql;
			this.logger = logger;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			if (logger.isDebugEnabled()) {
				con = ConnectionLogger.newInstance(con, logger);
			}
			return con.prepareStatement(this.sql);
		}

		@Override
		public String getSql() {
			return this.sql;
		}
	}
}
