package com.rabbitframework.jade.mapping;

/**
 * 实体属性字段类
 * 
 * @author Justin.liang
 *
 */
public class EntityProperty {
	private String property;
	private String column;
	private long length;
	private Class<?> javaType;
	private String selectKey;
	private GenerationType generationType;

	private EntityProperty() {
	}

	public static class Builder {
		private EntityProperty entityProperty = new EntityProperty();

		public Builder(String property, String column) {
			entityProperty.property = property;
			entityProperty.column = column;
		}

		public Builder generationType(GenerationType generationType) {
			entityProperty.generationType = generationType;
			return this;
		}

		public Builder length(long length) {
			entityProperty.length = length;
			return this;
		}

		public Builder javaType(Class<?> javaType) {
			entityProperty.javaType = javaType;
			return this;
		}

		public Builder selectKey(String selectKey) {
			entityProperty.selectKey = selectKey;
			return this;
		}

		public Builder property(String property) {
			entityProperty.property = property;
			return this;
		}

		public Builder column(String column) {
			entityProperty.column = column;
			return this;
		}

		public EntityProperty build() {
			return entityProperty;
		}
	}

	public String getSelectKey() {
		return selectKey;
	}

	public GenerationType getGenerationType() {
		return generationType;
	}

	public long getLength() {
		return length;
	}

	public String getProperty() {
		return property;
	}

	public String getColumn() {
		return column;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		EntityProperty that = (EntityProperty) o;
		if (property == null || !property.equals(that.property)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		if (property != null) {
			return property.hashCode();
		} else if (column != null) {
			return column.hashCode();
		} else {
			return 0;
		}
	}
}
