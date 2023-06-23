package com.scloudic.rabbitframework.jbatis.executor;


import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

import java.util.List;

public interface Executor {

    int update(MappedStatement ms, Object parameter,String dynamicSQL);

    int batchUpdate(MappedStatement ms, List<Object> parameter,String dynamicSQL);

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds,String dynamicSQL);
}
