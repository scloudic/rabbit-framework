package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

public class CustomerCreate extends BaseSQLParser {
    @Override
    public String parserSQL() {
        return sqlScript;
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.CREATE;
    }
}
