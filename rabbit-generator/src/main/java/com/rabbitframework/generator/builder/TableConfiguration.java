package com.rabbitframework.generator.builder;

import java.util.HashMap;
import java.util.Map;

public class TableConfiguration {
	private TableType tableType;
	private Map<String, String> tableMapping;
	private String filter;

	public TableConfiguration() {
		tableMapping = new HashMap<String, String>();
	}

	public TableType getTableType() {
		return tableType;
	}

	public void setTableType(TableType tableType) {
		this.tableType = tableType;
	}

	public void put(String tableName, String objectName) {
		tableMapping.put(tableName, objectName);
	}

	public void setTableMapping(Map<String, String> tableMapping) {
		this.tableMapping = tableMapping;
	}

	public Map<String, String> getTableMapping() {
		return tableMapping;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}
}
