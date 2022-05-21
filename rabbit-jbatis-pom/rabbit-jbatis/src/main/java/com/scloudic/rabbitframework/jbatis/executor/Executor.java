package com.scloudic.rabbitframework.jbatis.executor;


import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

import java.util.List;

public interface Executor {

    int update(MappedStatement ms, Object parameter);

    int batchUpdate(MappedStatement ms, List<Object> parameter);

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds);
}
