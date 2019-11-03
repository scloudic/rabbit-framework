package com.rabbitframework.jade.executor;

import com.rabbitframework.jade.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.jade.mapping.BoundSql;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;

public interface StatementHandler {
    BoundSql[] getBoundSql();

    int update(JdbcTemplateHolder jdbcTemplateHolder);

    <E> List<E> query(JdbcTemplateHolder jdbcTemplateHolder);

    PreparedStatementCreator createPreparedStatement();

    int batchUpdate(JdbcTemplateHolder jdbcTemplateHolder);
}
