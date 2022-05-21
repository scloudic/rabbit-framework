package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.EntityProperty;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

import java.util.List;

public class UpdateByParams extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        String sql = getUpdateSqlByWhere(entityMap);
        return sql + " " + getSearchSql();
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.UPDATE;
    }

    private String getUpdateSqlByWhere(EntityMap entityMap) {
        StringBuilder sbPrefix = new StringBuilder();
        List<EntityProperty> propertyMapping = entityMap.getColumnProperties();
        sbPrefix.append("update ");
        sbPrefix.append(entityMap.getTableName());
        sbPrefix.append("${tableSuffix}");
        sbPrefix.append(" ");
        sbPrefix.append("<trim prefix=\"set \" suffixOverrides=\",\" >");
        for (EntityProperty entityMapping : propertyMapping) {
            String column = entityMapping.getColumn();
            String property = entityMapping.getProperty();
            sbPrefix.append("<if test=\"params." + property + " != null\" >").append(column).append("=").append("#{params.")
                    .append(property).append("}").append(",").append("</if>");
        }
        sbPrefix.append("</trim>");
        sbPrefix.append(" where 1=1 ");

        String updateSqlScript = sbPrefix.toString();
        return updateSqlScript;
    }
}
