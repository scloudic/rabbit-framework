package com.rabbitframework.jade.scripting;

import com.rabbitframework.jade.mapping.BoundSql;
import com.rabbitframework.jade.mapping.RowBounds;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds);
}
