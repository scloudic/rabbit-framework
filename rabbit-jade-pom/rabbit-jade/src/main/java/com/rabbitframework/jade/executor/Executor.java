package com.rabbitframework.jade.executor;


import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.mapping.RowBounds;

import java.util.List;

public interface Executor {

    int update(MappedStatement ms, Object parameter);

    int batchUpdate(MappedStatement ms, List<Object> parameter);

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds);
}
