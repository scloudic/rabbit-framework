package com.scloudic.rabbitframework.jbatis.builder;

import java.util.Properties;

import com.scloudic.rabbitframework.jbatis.exceptions.BindingException;
import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.jbatis.reflect.SystemMetaObject;
import com.scloudic.rabbitframework.core.propertytoken.PropertyParser;

public class PropertiesConvert {
	public static void setProperties(Properties properties, Object metaData,
			Properties propertiesValue) {
		MetaObject metaDataSource = SystemMetaObject.forObject(metaData);
		for (Object key : properties.keySet()) {
			String propertyName = (String) key;
			if (metaDataSource.hasSetter(propertyName)) {
				String value = (String) properties.get(propertyName);
				if (propertiesValue != null) {
					value = PropertyParser.parseDollar(value, propertiesValue);
				}
				Object convertedValue = convertValue(metaDataSource,
						propertyName, value);
				metaDataSource.setValue(propertyName, convertedValue);
			} else {
				throw new BindingException("Unknown " + metaData
						+ " property: " + propertyName);
			}
		}
	}

	private static Object convertValue(MetaObject metaDataSource,
			String propertyName, String value) {
		Object convertedValue = value;
		Class<?> targetType = metaDataSource.getSetterType(propertyName);
		if (targetType == Integer.class || targetType == int.class) {
			convertedValue = Integer.valueOf(value);
		} else if (targetType == Long.class || targetType == long.class) {
			convertedValue = Long.valueOf(value);
		} else if (targetType == Boolean.class || targetType == boolean.class) {
			convertedValue = Boolean.valueOf(value);
		}
		return convertedValue;
	}
}
