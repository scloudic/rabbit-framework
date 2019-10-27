package com.rabbitframework.generator.dataaccess;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.generator.builder.Configuration;
import com.rabbitframework.generator.builder.TableConfiguration;
import com.rabbitframework.generator.builder.TableType;
import com.rabbitframework.generator.exceptions.GeneratorException;
import com.rabbitframework.generator.mapping.EntityMapping;
import com.rabbitframework.generator.mapping.EntityProperty;
import com.rabbitframework.generator.mapping.type.FullyQualifiedJavaType;
import com.rabbitframework.generator.mapping.type.JavaTypeResolver;
import com.tjzq.commons.utils.StringUtils;

public class DatabaseIntrospector {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseIntrospector.class);
	private DatabaseMetaData databaseMetaData;
	private Configuration configuration;

	public DatabaseIntrospector(DatabaseMetaData databaseMetaData, Configuration configuration) {
		this.databaseMetaData = databaseMetaData;
		this.configuration = configuration;
	}

	public List<EntityMapping> introspectTables() {
		logger.debug("call introspectTables();");
		List<EntityMapping> entityMappings = null;
		try {
			// 获取产品名称
			String productName = databaseMetaData.getDatabaseProductName();
			logger.debug("productName:" + productName);
			TableConfiguration tableConfiguration = configuration.getTableConfiguration();
			TableType tableType = tableConfiguration.getTableType();
			if (tableType == TableType.ALL) {
				tableConfiguration.setTableMapping(getTablesName(tableConfiguration));
			}
			entityMappings = introspectTables(tableConfiguration);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new GeneratorException(e);
		}
		return entityMappings;
	}

	private List<EntityMapping> introspectTables(TableConfiguration tableConfiguration) {
		Map<String, String> tableMapping = tableConfiguration.getTableMapping();
		List<EntityMapping> entityMappings = new ArrayList<EntityMapping>();
		Set<Map.Entry<String, String>> sets = tableMapping.entrySet();
		for (Map.Entry<String, String> entry : sets) {
			String tableName = entry.getKey();
			String objectName = entry.getValue();

			Map<String, EntityProperty> entityPropertyMap = getColumns(tableName);
			calculateExtraColumnInformation(entityPropertyMap);
			calculatePrimaryKey(tableName, entityPropertyMap);
			List<EntityProperty> entityProperties = new ArrayList<EntityProperty>(entityPropertyMap.values());
			EntityMapping.Builder builder = new EntityMapping.Builder(tableName, objectName, entityProperties);
			entityMappings.add(builder.build());
		}
		return entityMappings;
	}

	private void calculatePrimaryKey(String tableName, Map<String, EntityProperty> entityPropertyMap) {
		ResultSet resultSet = null;
		try {
			String catalog = configuration.getJdbcConnectionInfo().getCatalog();
			resultSet = databaseMetaData.getPrimaryKeys(catalog, null, tableName);
			while (resultSet.next()) {
				String columnName = resultSet.getString("COLUMN_NAME");
				EntityProperty entityProperty = entityPropertyMap.get(columnName);
				if (entityProperty != null) {
					entityProperty.setPrimaryKey(true);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new GeneratorException(e);
		} finally {
			close(resultSet);
		}
	}

	private void calculateExtraColumnInformation(Map<String, EntityProperty> entityPropertyMap) {
		JavaTypeResolver javaTypeResolver = configuration.getJavaTypeResolver();
		for (Map.Entry<String, EntityProperty> entityPropertyEntry : entityPropertyMap.entrySet()) {
			EntityProperty entityProperty = entityPropertyEntry.getValue();
			FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver.calculateJavaType(entityProperty);
			if (fullyQualifiedJavaType != null) {
				entityProperty.setJavaType(fullyQualifiedJavaType);
				entityProperty.setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(entityProperty));
			} else {
				entityProperty.setJavaType(FullyQualifiedJavaType.getObjectInstance());
			}
			entityProperty.setJavaProperty(StringUtils.toCamelCase(entityProperty.getColumnName(), false));
		}
	}

	public Map<String, EntityProperty> getColumns(String tableName) {
		ResultSet resultSet = null;
		Map<String, EntityProperty> entityPropertyMap = new HashMap<String, EntityProperty>();
		try {
			resultSet = databaseMetaData.getColumns(null, null, tableName, "%");
			while (resultSet.next()) {
				EntityProperty tableColumn = new EntityProperty();
				int jdbcType = resultSet.getInt("DATA_TYPE");
				int length = resultSet.getInt("COLUMN_SIZE");
				String columnName = resultSet.getString("COLUMN_NAME");
				boolean nullable = resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
				int scale = resultSet.getInt("DECIMAL_DIGITS");
				String remarks = resultSet.getString("REMARKS");
				String defaultValue = resultSet.getString("COLUMN_DEF");
				String tableCat = resultSet.getString("TABLE_CAT");
				String tableSchem = resultSet.getString("TABLE_SCHEM");
				String tableNameDb = resultSet.getString("TABLE_NAME");
				logger.debug("jdbcType:" + jdbcType + ",length:" + length + ",columnName:" + columnName + ",nullable:"
						+ nullable + ",scale:" + scale + ",remarks:" + remarks + "defaultValue:" + defaultValue
						+ ",tableCat:" + tableCat + ",tableSchem:" + tableSchem + ",tableNameDb:" + tableNameDb);
				tableColumn.setJdbcType(jdbcType);
				tableColumn.setLength(length);
				tableColumn.setColumnName(columnName.toLowerCase(Locale.US));
				tableColumn.setNullable(nullable);
				tableColumn.setScale(scale);
				tableColumn.setRemarks(remarks);
				tableColumn.setDefaultValue(defaultValue);
				entityPropertyMap.put(columnName, tableColumn);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new GeneratorException(e);
		} finally {
			close(resultSet);
		}
		return entityPropertyMap;
	}

	public void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e) {
			}
		}
	}

	private Map<String, String> getTablesName(TableConfiguration tableConfiguration) {
		ResultSet resultSet = null;
		String filter = tableConfiguration.getFilter();
		Map<String, String> map = new HashMap<String, String>();
		try {
			String catalog = configuration.getJdbcConnectionInfo().getCatalog();
			if (StringUtils.isBlank(catalog)) {
				catalog = null;
			}
			resultSet = databaseMetaData.getTables(catalog, null, null, new String[] { "TABLE" });
			while (resultSet.next()) {
				String tableName = resultSet.getString("TABLE_NAME");
				if (StringUtils.isNotBlank(filter)) {
					if (tableName.substring(0, filter.length()).equals(filter)) {
						continue;
					}
				}
				String objectName = StringUtils.toCamelCase(tableName, true);
				map.put(tableName, objectName);
			}
		} catch (Exception e) {

		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
		}
		return map;
	}
}
