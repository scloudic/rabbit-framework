package com.rabbitframework.dbase.scripting;

import com.rabbitframework.dbase.mapping.BoundSql;
import com.rabbitframework.dbase.mapping.RowBounds;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds);
}
