package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

public class SelectByParams extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        String sql = String.format(sqlScript, entityMap.getTableName());
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(" ");
        sb.append(getSearchSql());
        sb.append(" ");
        sb.append("<if test=\"orderBy != null\" > order by ${orderBy} </if>");
        return sb.toString();
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.SELECT;
    }
}
