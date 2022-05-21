package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

public class SelectOneByParams extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        String sql = String.format(sqlScript, entityMap.getTableName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sql);
        stringBuilder.append(" ");
        stringBuilder.append(getSearchSql());
        stringBuilder.append(" ");
        return stringBuilder.toString();
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.SELECT;
    }
}
