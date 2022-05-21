package com.scloudic.rabbitframework.jbatis.executor;


import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 在执行PreparedStatement时,参数处理接口.
 */
public interface ParameterHandler {
    void setParameters(PreparedStatement ps) throws SQLException;

}
