package com.rabbitframework.dbase.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.slf4j.Logger;

/*
 * Connection proxy to add logging
 */
public final class ConnectionLogger extends BaseJdbcLogger implements
		InvocationHandler {

	private Connection connection;

	private ConnectionLogger(Connection conn, Logger statementLog) {
		super(statementLog);
		this.connection = conn;
		if (isDebugEnabled()) {
			debug("ooo Using Connection [" + conn + "]");
		}
	}

	public Object invoke(Object proxy, Method method, Object[] params)
			throws Throwable {
		try {
			if ("prepareStatement".equals(method.getName())) {
				if (isDebugEnabled()) {
					debug("==>  Preparing: "+ removeBreakingWhitespace((String) params[0]));
				}
				PreparedStatement stmt = (PreparedStatement) method.invoke(connection, params);
				stmt = PreparedStatementLogger.newInstance(stmt,getStatementLog());
				return stmt;
			} else if ("prepareCall".equals(method.getName())) {
				if (isDebugEnabled()) {
					debug("==>  Preparing: "
							+ removeBreakingWhitespace((String) params[0]));
				}
				PreparedStatement stmt = (PreparedStatement) method.invoke(
						connection, params);
				stmt = PreparedStatementLogger.newInstance(stmt,
						getStatementLog());
				return stmt;
			} else if ("createStatement".equals(method.getName())) {
				Statement stmt = (Statement) method.invoke(connection, params);
				stmt = StatementLogger.newInstance(stmt, getStatementLog());
				return stmt;
			} else if ("close".equals(method.getName())) {
				if (isDebugEnabled()) {
					debug("xxx Connection Closed");
				}
				return method.invoke(connection, params);
			} else {
				return method.invoke(connection, params);
			}
		} catch (Throwable t) {
			throw t;
		}
	}

	/*
	 * Creates a logging version of a connection
	 * 
	 * @param conn - the original connection
	 * 
	 * @return - the connection with logging
	 */
	public static Connection newInstance(Connection conn, Logger statementLog) {
		InvocationHandler handler = new ConnectionLogger(conn, statementLog);
		ClassLoader cl = Connection.class.getClassLoader();
		return (Connection) Proxy.newProxyInstance(cl,
				new Class[] { Connection.class }, handler);
	}

	/*
	 * return the wrapped connection
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

}
