package com.rabbitframework.dbase.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EntityMap {
	private String id;
	private Class<?> type;
	private String tableName;
	private List<EntityProperty> entityProperties;
	private List<EntityProperty> idProperties;
	private List<EntityProperty> columnProperties;
	private Set<String> mappedColumns;

	private EntityMap() {
	}

	public static class Builder {
		EntityMap entityMap = new EntityMap();

		public Builder(String id, String tableName, Class<?> type,
				List<EntityProperty> entityProperties) {
			entityMap.id = id;
			entityMap.type = type;
			entityMap.tableName = tableName;
			entityMap.entityProperties = entityProperties;
		}

		public EntityMap build() {
			entityMap.mappedColumns = new HashSet<String>();
			entityMap.idProperties = new ArrayList<EntityProperty>();
			entityMap.columnProperties = new ArrayList<EntityProperty>();
			for (EntityProperty entityProperty : entityMap.entityProperties) {
				entityMap.mappedColumns.add(entityProperty.getColumn()
						.toUpperCase(Locale.ENGLISH));
				if (entityProperty.getGenerationType() == null) {
					entityMap.columnProperties.add(entityProperty);
				} else {
					entityMap.idProperties.add(entityProperty);
				}
			}
			entityMap.mappedColumns = Collections
					.unmodifiableSet(entityMap.mappedColumns);
			entityMap.idProperties = Collections
					.unmodifiableList(entityMap.idProperties);
			entityMap.columnProperties = Collections
					.unmodifiableList(entityMap.columnProperties);
			entityMap.entityProperties = Collections
					.unmodifiableList(entityMap.entityProperties);
			return entityMap;
		}
	}

	public String getId() {
		return id;
	}

	public Class<?> getType() {
		return type;
	}

	public String getTableName() {
		return tableName;
	}

	public List<EntityProperty> getIdProperties() {
		return idProperties;
	}

	public List<EntityProperty> getColumnProperties() {
		return columnProperties;
	}
}
