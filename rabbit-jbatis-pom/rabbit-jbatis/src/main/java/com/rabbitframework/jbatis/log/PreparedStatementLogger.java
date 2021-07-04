package com.rabbitframework.jbatis.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;

/*
 * PreparedStatement proxy to add logging
 */
public final class PreparedStatementLogger extends BaseJdbcLogger implements InvocationHandler {

    private PreparedStatement statement;

    private PreparedStatementLogger(PreparedStatement stmt, Logger statementLog) {
        super(statementLog);
        this.statement = stmt;
    }

    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (EXECUTE_METHODS.contains(method.getName())) {
                if (isDebugEnabled()) {
                    debug(statementLog.getName() + " ==> Parameters: " + getParameterValueString());
                }
                clearColumnInfo();
                if ("executeQuery".equals(method.getName())) {
                    ResultSet rs = (ResultSet) method.invoke(statement, params);
                    if (rs != null) {
                        return ResultSetLogger.newInstance(rs, getStatementLog());
                    } else {
                        return null;
                    }
                } else {
                    return method.invoke(statement, params);
                }
            } else if (SET_METHODS.contains(method.getName())) {
                if ("setNull".equals(method.getName())) {
                    setColumn(params[0], null);
                } else {
                    setColumn(params[0], params[1]);
                }
                return method.invoke(statement, params);
            } else if ("getResultSet".equals(method.getName())) {
                ResultSet rs = (ResultSet) method.invoke(statement, params);
                if (rs != null) {
                    return ResultSetLogger.newInstance(rs, getStatementLog());
                } else {
                    return null;
                }
            } else if ("getUpdateCount".equals(method.getName())) {
                int updateCount = (Integer) method.invoke(statement, params);
                if (updateCount != -1) {
                    debug(statementLog.getName() + " <== Updates: " + updateCount);
                }
                return updateCount;
            } else if ("equals".equals(method.getName())) {
                Object ps = params[0];
                return ps instanceof Proxy && proxy == ps;
            } else if ("hashCode".equals(method.getName())) {
                return proxy.hashCode();
            } else {
                return method.invoke(statement, params);
            }
        } catch (Throwable t) {
            throw t;
        }
    }

    /*
     * Creates a logging version of a PreparedStatement
     *
     * @param stmt - the statement
     * @param sql  - the sql statement
     * @return - the proxy
     */
    public static PreparedStatement newInstance(PreparedStatement stmt, Logger statementLog) {
        InvocationHandler handler = new PreparedStatementLogger(stmt, statementLog);
        ClassLoader cl = PreparedStatement.class.getClassLoader();
        return (PreparedStatement) Proxy.newProxyInstance(cl, new Class[]{PreparedStatement.class, CallableStatement.class}, handler);
    }

    /*
     * Return the wrapped prepared statement
     *
     * @return the PreparedStatement
     */
    public PreparedStatement getPreparedStatement() {
        return statement;
    }

}
