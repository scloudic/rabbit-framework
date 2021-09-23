package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.EntityProperty;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import java.util.List;
public class UpdateByEntity extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        return getUpdateSql(entityMap);
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.UPDATE;
    }

    private String getUpdateSql(EntityMap entityMap) {
        StringBuilder sbPrefix = new StringBuilder();
        List<EntityProperty> propertyMapping = entityMap.getColumnProperties();
        sbPrefix.append("update ");
        sbPrefix.append(entityMap.getTableName());
        sbPrefix.append(" ");
        sbPrefix.append("<trim prefix=\"set \" suffixOverrides=\",\" >");

        for (EntityProperty entityMapping : propertyMapping) {
            String column = entityMapping.getColumn();
            String property = entityMapping.getProperty();
            sbPrefix.append("<if test=\"" + property + " != null\" >").append(column).append("=").append("#{")
                    .append(property).append("}").append(",").append("</if>");
        }
        sbPrefix.append("</trim>");
        sbPrefix.append(" ");
        sbPrefix.append(" where ");
        sbPrefix.append("<trim suffix=\" \" suffixOverrides=\"and\" >");
        List<EntityProperty> idMapping = entityMap.getIdProperties();
        for (EntityProperty entityMapping : idMapping) {
            String column = entityMapping.getColumn();
            String property = entityMapping.getProperty();
            sbPrefix.append(column).append("=").append("#{").append(property).append("}").append(" and ");
        }
        sbPrefix.append("</trim>");
        String updateSqlScript = sbPrefix.toString();
        return updateSqlScript;
    }
}
