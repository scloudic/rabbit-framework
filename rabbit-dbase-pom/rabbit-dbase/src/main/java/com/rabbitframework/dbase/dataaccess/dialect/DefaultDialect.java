package com.rabbitframework.dbase.dataaccess.dialect;

import java.util.Locale;

import com.tjzq.commons.utils.ClassUtils;

/**
 * @author: justin.liang
 * @date: 16/5/5 下午9:22
 */
public enum DefaultDialect {
	mysql(MySqlDialect.class), oracle(OracleDialect.class);
	private final Class<? extends Dialect> dialect;

	private DefaultDialect(Class<? extends Dialect> dialect) {
		this.dialect = dialect;
	}

	public Class<? extends Dialect> getDialect() {
		return dialect;
	}

	public Dialect newInstances() {
		return (Dialect) ClassUtils.newInstance(this.dialect);
	}

	public static Dialect newInstances(String dialect) {
		DefaultDialect defaultDialect = getDefaultDialect(dialect);
		if (defaultDialect == null) {
			return (Dialect) ClassUtils.newInstance(dialect);
		}
		return defaultDialect.newInstances();
	}

	public static DefaultDialect getDefaultDialect(String dialect) {
		String param = dialect.toLowerCase(Locale.ENGLISH);
		if (DefaultDialect.mysql.name().equals(param)) {
			return mysql;
		} else if (DefaultDialect.oracle.name().equals(param)) {
			return oracle;
		} else {
			return null;
		}
	}
}
