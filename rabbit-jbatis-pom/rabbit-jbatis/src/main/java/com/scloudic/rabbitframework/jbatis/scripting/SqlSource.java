package com.scloudic.rabbitframework.jbatis.scripting;

import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds);
}
