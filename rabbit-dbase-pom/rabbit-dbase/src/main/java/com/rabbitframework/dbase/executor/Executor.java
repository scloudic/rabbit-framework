package com.rabbitframework.dbase.executor;


import com.rabbitframework.dbase.mapping.MappedStatement;
import com.rabbitframework.dbase.mapping.RowBounds;

import java.util.List;

public interface Executor {

    int update(MappedStatement ms, Object parameter);

    int batchUpdate(MappedStatement ms, List<Object> parameter);

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds);
}
