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
    private String dialect;
    private boolean delStatus = false;

    public static class Builder {
        EntityMapping entityMapping = new EntityMapping();

        public Builder(String tableName, String objectName,
                       List<EntityProperty> entityProperties,
                       String dialect) {
            entityMapping.tableName = tableName;
            entityMapping.dialect = dialect.toLowerCase();
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
                    if (entityProperty.isAutoincrement()) {
                        if ("oracle".equalsIgnoreCase(entityMapping.dialect)) {
                            packages.add("com.rabbitframework.jbatis.mapping.GenerationType");
                        }
                    } else {
                        packages.add("com.rabbitframework.jbatis.mapping.GenerationType");
                    }

                } else {
                    entityMapping.columnProperties.add(entityProperty);
                }
                if ("del_status".equals(entityProperty.getColumnName()) && entityMapping.delStatus == false) {
                    entityMapping.delStatus = true;
                    packages.add("java.beans.Transient");
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

    public String getDialect() {
        return dialect;
    }

    public boolean isDelStatus() {
        return delStatus;
    }
}
