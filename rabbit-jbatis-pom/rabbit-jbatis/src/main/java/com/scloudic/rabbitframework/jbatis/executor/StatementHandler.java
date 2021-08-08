package com.scloudic.rabbitframework.jbatis.executor;

import com.scloudic.rabbitframework.jbatis.dataaccess.JdbcTemplateHolder;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;

public interface StatementHandler {
    BoundSql[] getBoundSql();

    int update(JdbcTemplateHolder jdbcTemplateHolder);

    <E> List<E> query(JdbcTemplateHolder jdbcTemplateHolder);

    PreparedStatementCreator createPreparedStatement();

    int batchUpdate(JdbcTemplateHolder jdbcTemplateHolder);
}
