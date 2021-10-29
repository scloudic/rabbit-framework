package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

public class SelectDynamicTableById extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        //只考虑一个主键的情况,默认取第一个
        String id = entityMap.getIdProperties().get(0).getColumn();
        if (StringUtils.isBlank(id)) {
            throw new BuilderException("primary key is null");
        }
        return String.format(sqlScript, entityMap.getTableName(), id);
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.SELECT;
    }
}
