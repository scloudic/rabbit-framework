package com.rabbitframework.jade.executor;


import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 在执行PreparedStatement时,参数处理接口.
 */
public interface ParameterHandler {
    /**
     * sql执行前的参数设置绑定
     *
     * @param ps
     * @throws SQLException
     */
    void setParameters(PreparedStatement ps) throws SQLException;

}
