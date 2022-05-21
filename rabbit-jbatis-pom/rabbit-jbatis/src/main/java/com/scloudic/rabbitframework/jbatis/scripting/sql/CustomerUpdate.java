package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

public class CustomerUpdate extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (paramType != Where.class) {
            return sqlScript;
        }
        String sqlValue = sqlScript + " " + getSearchSql() + " ";
        return sqlValue;
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.UPDATE;
    }
}
