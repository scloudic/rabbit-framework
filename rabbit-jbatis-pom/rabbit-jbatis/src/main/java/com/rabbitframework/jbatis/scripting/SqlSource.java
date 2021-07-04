package com.rabbitframework.jbatis.scripting;

import com.rabbitframework.jbatis.mapping.BoundSql;
import com.rabbitframework.jbatis.mapping.RowBounds;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds);
}
