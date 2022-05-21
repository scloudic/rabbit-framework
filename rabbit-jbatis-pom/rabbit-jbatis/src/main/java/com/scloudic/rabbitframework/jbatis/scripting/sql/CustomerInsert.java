package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.dataaccess.KeyGenerator;
import com.scloudic.rabbitframework.jbatis.mapping.EntityProperty;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.mapping.binding.EntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class CustomerInsert extends BaseSQLParser {
    @Override
    public String parserSQL() {
        return sqlScript;
    }

    public List<KeyGenerator> getKeyGenerators() {
        List<KeyGenerator> keyGenerators = new ArrayList<>();
        String paramTypeName = paramType.getName();
        EntityRegistry entityRegistry = configuration.getEntityRegistry();
        boolean isEntity = entityRegistry.hasEntityMap(paramTypeName);
        if (!isEntity) {
            return keyGenerators;
        }
        List<EntityProperty> idEntityMapping = entityRegistry.getEntityMap(paramTypeName).getIdProperties();
        for (EntityProperty entityProperty : idEntityMapping) {
            KeyGenerator keyGenerator = new KeyGenerator(entityProperty.getGenerationType(),
                    entityProperty.getJavaType(), entityProperty.getProperty(), entityProperty.getColumn(),
                    entityProperty.getSelectKey());
            keyGenerators.add(keyGenerator);
        }
        return keyGenerators;
    }

    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.INSERT;
    }
}
