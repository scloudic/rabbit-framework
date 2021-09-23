package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.dataaccess.KeyGenerator;
import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.EntityProperty;
import com.scloudic.rabbitframework.jbatis.mapping.GenerationType;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.mapping.binding.EntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class BatchInsertByEntity extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (genericClass == null) {
            throw new BuilderException("genericClass is null");
        }
        String paramTypeName = genericClass.getName();
        EntityMap entityMap = getEntityMap(paramTypeName);
        return getBatchInsertSql(entityMap);
    }

    public List<KeyGenerator> getKeyGenerators() {
        List<KeyGenerator> keyGenerators = new ArrayList<>();
        String paramTypeName = genericClass.getName();
        List<EntityProperty> idEntityMapping = getEntityMap(paramTypeName).getIdProperties();
        for (EntityProperty entityProperty : idEntityMapping) {
            KeyGenerator keyGenerator = new KeyGenerator(entityProperty.getGenerationType(),
                    entityProperty.getJavaType(), entityProperty.getProperty(), entityProperty.getColumn(),
                    entityProperty.getSelectKey());
            keyGenerators.add(keyGenerator);
        }
        return keyGenerators;
    }

    private String getBatchInsertSql(EntityMap entityMap) {
        StringBuilder sbPrefix = new StringBuilder();
        sbPrefix.append(" insert into ");
        sbPrefix.append(entityMap.getTableName());
        sbPrefix.append(" ");
        sbPrefix.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        StringBuilder sbSuffix = new StringBuilder();
        sbSuffix.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        List<EntityProperty> identityMapping = entityMap.getIdProperties();
        for (EntityProperty entityMapping : identityMapping) {
            String column = entityMapping.getColumn();
            String property = entityMapping.getProperty();
            GenerationType genType = entityMapping.getGenerationType();
            if (GenerationType.IDENTITY.equals(genType)) {
                continue;
            }
            sbPrefix.append(column);
            sbPrefix.append(",");
            if (GenerationType.SEQUENCE.equals(genType)) {
                sbSuffix.append(entityMapping.getSelectKey());
            } else {
                sbSuffix.append("#{");
                sbSuffix.append(property);
                sbSuffix.append("}");
            }
            sbSuffix.append(",");
        }
        List<EntityProperty> propertyMapping = entityMap.getColumnProperties();
        for (EntityProperty entityMapping : propertyMapping) {
            String column = entityMapping.getColumn();
            String property = entityMapping.getProperty();
            sbPrefix.append(column);
            sbPrefix.append(",");
            sbSuffix.append("#{");
            sbSuffix.append(property);
            sbSuffix.append("}");
            sbSuffix.append(",");
        }
        sbPrefix.append("</trim>");
        sbSuffix.append("</trim>");
        String insertSql = sbPrefix.toString() + sbSuffix.toString();
        return insertSql;
    }

    @Override
    public boolean isBatchUpdate() {
        return true;
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.INSERT;
    }
}
