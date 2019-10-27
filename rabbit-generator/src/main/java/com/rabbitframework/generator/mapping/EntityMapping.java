package com.rabbitframework.generator.mapping;

import com.rabbitframework.generator.mapping.type.FullyQualifiedJavaType;

import java.util.*;

public class EntityMapping {
    private List<String> importPackage;
    private String tableName;
    private String objectName;
    private List<EntityProperty> entityProperties;
    private List<EntityProperty> idProperties;
    private List<EntityProperty> columnProperties;

    public static class Builder {
        EntityMapping entityMapping = new EntityMapping();

        public Builder(String tableName, String objectName, List<EntityProperty> entityProperties) {
            entityMapping.tableName = tableName;
            entityMapping.objectName = objectName;
            entityMapping.entityProperties = entityProperties;
        }

        public EntityMapping build() {
            entityMapping.idProperties = new ArrayList<EntityProperty>();
            entityMapping.columnProperties = new ArrayList<EntityProperty>();
            Set<String> packages = new HashSet<String>();
            for (EntityProperty entityProperty : entityMapping.entityProperties) {
                if (entityProperty.isPrimaryKey()) {
                    entityMapping.idProperties.add(entityProperty);
                } else {
                    entityMapping.columnProperties.add(entityProperty);
                }
                FullyQualifiedJavaType javaType = entityProperty.getJavaType();
                if (!javaType.isPrimitive()) {
                    packages.add(javaType.getFullName());
                }
            }
            entityMapping.idProperties = Collections.unmodifiableList(entityMapping.idProperties);
            entityMapping.columnProperties = Collections.unmodifiableList(entityMapping.columnProperties);
            entityMapping.importPackage = new ArrayList<String>(packages);
            entityMapping.importPackage = Collections.unmodifiableList(entityMapping.importPackage);
            return entityMapping;
        }
    }


    public List<EntityProperty> getColumnProperties() {
        return columnProperties;
    }

    public List<EntityProperty> getIdProperties() {
        return idProperties;
    }

    public List<String> getImportPackage() {
        return importPackage;
    }

    public String getTableName() {
        return tableName;
    }

    public String getObjectName() {
        return objectName;
    }

    public List<EntityProperty> getEntityProperties() {
        return entityProperties;
    }
}
