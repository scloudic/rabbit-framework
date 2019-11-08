package com.rabbitframework.jbatis.log;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;

/*
 * Statement proxy to add logging
 */
public final class StatementLogger extends BaseJdbcLogger implements InvocationHandler {

  private Statement statement;

  private StatementLogger(Statement stmt, Logger statementLog) {
    super(statementLog);
    this.statement = stmt;
  }

  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
    try {
      if (EXECUTE_METHODS.contains(method.getName())) {
        if (isDebugEnabled()) {
          debug("==>  Executing: " + removeBreakingWhitespace((String) params[0]));
        }
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
      } else if ("getResultSet".equals(method.getName())) {
        ResultSet rs = (ResultSet) method.invoke(statement, params);
        if (rs != null) {
          return ResultSetLogger.newInstance(rs, getStatementLog());
        } else {
          return null;
        }
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
   * Creates a logging version of a Statement
   *
   * @param stmt - the statement
   * @return - the proxy
   */
  public static Statement newInstance(Statement stmt, Logger statementLog) {
    InvocationHandler handler = new StatementLogger(stmt, statementLog);
    ClassLoader cl = Statement.class.getClassLoader();
    return (Statement) Proxy.newProxyInstance(cl, new Class[]{Statement.class}, handler);
  }

  /*
   * return the wrapped statement
   *
   * @return the statement
   */
  public Statement getStatement() {
    return statement;
  }

}
