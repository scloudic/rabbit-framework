package com.rabbitframework.jbatis.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.rabbitframework.jbatis.annontations.Column;
import com.rabbitframework.jbatis.annontations.ID;
import com.rabbitframework.jbatis.annontations.Table;
import com.rabbitframework.jbatis.mapping.EntityMap;
import com.rabbitframework.jbatis.mapping.EntityProperty;
import com.rabbitframework.jbatis.mapping.GenerationType;
import com.rabbitframework.jbatis.reflect.MetaClass;
import com.rabbitframework.core.utils.StringUtils;

/**
 * 实体类解析
 *
 * @author Justin Liang
 */
public class EntityBuilder extends BaseBuilder {
	public EntityBuilder(Configuration configuration) {
		super(configuration);
	}

	public EntityMap parseEntity(Class<?> entity) {
		String entityId = entity.getName(); // 获取完整类名当唯一标识
		Table table = entity.getAnnotation(Table.class); // 获取当前实体表名
		if (table == null) {
			return null;
		}
		String tableName = table.name();
		if (StringUtils.isBlank(tableName)) {
			tableName = StringUtils.toUnderScoreCase(entity.getSimpleName());
		}
		List<EntityProperty> entityProperties = new ArrayList<EntityProperty>();
		parserEntityProperty(entity, entityProperties);
		EntityMap entityMap = new EntityMap.Builder(entityId, tableName, entity, entityProperties).build();
		return entityMap;
	}

	private void parserEntityProperty(Class<?> entity, List<EntityProperty> entityProperties) {
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			String property = field.getName();
			ID id = field.getAnnotation(ID.class);
			Column column = field.getAnnotation(Column.class);
			if (id == null && column == null) {
				continue;
			}

			Class<?> javaType = null;
			long length = 255;
			String columnName = "";
			GenerationType generationType = null;
			String selectKey = "";
			if (id != null) {
				generationType = id.keyType();
				selectKey = id.selectKey();
				columnName = id.column();
				length = id.length(); // 长度
			} else if (column != null) {
				columnName = column.column();
				length = column.length(); // 长度
			}
			if (StringUtils.isBlank(columnName)) {
				columnName = StringUtils.toUnderScoreCase(property);
			}
			EntityProperty entityProperty = buildEntityProperty(property, entity, length, javaType, selectKey,
					generationType, columnName);
			entityProperties.add(entityProperty);
		}
		if (entity.getSuperclass() != null) {
			parserEntityProperty(entity.getSuperclass(), entityProperties);
		}
	}

	private EntityProperty buildEntityProperty(String property, Class<?> entity, long length, Class<?> javaType,
			String selectKey, GenerationType generationType, String column) {
		Class<?> javaTypeClass = resolveResultJavaType(entity, property, javaType);
		EntityProperty.Builder builder = new EntityProperty.Builder(property, column);
		builder.javaType(javaTypeClass);
		builder.length(length);
		builder.generationType(generationType);
		builder.selectKey(selectKey);
		return builder.build();
	}

	/**
	 * 获取javaType类型
	 *
	 * @param resultType
	 * @param property
	 * @param javaType
	 * @return
	 */
	private Class<?> resolveResultJavaType(Class<?> resultType, String property, Class<?> javaType) {
		if ((javaType == null) && property != null) {
			try {
				MetaClass metaResultType = MetaClass.forClass(resultType);
				javaType = metaResultType.getSetterType(property);
			} catch (Exception e) {
				// ignore
			}
		}
		if (javaType == null) {
			javaType = Object.class;
		}
		return javaType;
	}

}
