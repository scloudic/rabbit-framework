package com.rabbitframework.dbase.executor;

import com.rabbitframework.dbase.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.dbase.mapping.BoundSql;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;

public interface StatementHandler {
    BoundSql[] getBoundSql();

    int update(JdbcTemplateHolder jdbcTemplateHolder);

    <E> List<E> query(JdbcTemplateHolder jdbcTemplateHolder);

    PreparedStatementCreator createPreparedStatement();

    int batchUpdate(JdbcTemplateHolder jdbcTemplateHolder);
}
