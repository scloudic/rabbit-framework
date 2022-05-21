package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

public class DeleteByParams extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        String sql = String.format(sqlScript, entityMap.getTableName());
        sql = sql + " " + getSearchSql() + " ";
        return sql;

    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.DELETE;
    }
}
