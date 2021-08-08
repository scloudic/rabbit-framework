package com.scloudic.rabbitframework.jbatis.mapping.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.scloudic.rabbitframework.jbatis.reflect.MetaClass;
import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.jbatis.reflect.SystemMetaObject;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import com.scloudic.rabbitframework.core.utils.StringUtils;

@SuppressWarnings("rawtypes")
public class BeanPropertyRowMapper implements RowMapper {
	private final Class<?> mappedClass;
	private Map<String, String> mappedFields;

	public BeanPropertyRowMapper(Class<?> mappedClass) {
		this.mappedClass = mappedClass;
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		initialize();
	}

	/**
	 * Initialize the mapping metadata for the given class.
	 */
	protected void initialize() {
		this.mappedFields = new HashMap<String, String>();
		MetaClass metaClass = MetaClass.forClass(mappedClass);
		String[] propertyNames = metaClass.getSetterNames();
		for (String property : propertyNames) {
			String fields = StringUtils.toUnderScoreCase(property);
			mappedFields.put(fields, property);
		}

	}

	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Object mappedObject = instantiateClass(this.mappedClass);
		MetaObject metaObject = SystemMetaObject.forObject(mappedObject);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index).toLowerCase(Locale.ENGLISH);
			String property = mappedFields.get(column);
			if (StringUtils.isBlank(property)) {
				property = column;
			}
			if (metaObject.hasSetter(property)) {
				Class<?> type = metaObject.getSetterType(property);
				Object obj = JdbcUtils.getResultSetValue(rs, index, type);
				if (obj != null) {
					metaObject.setValue(property, obj);
				}
			}
		}
		return mappedObject;
	}

	private static Object instantiateClass(Class<?> clazz) throws BeanInstantiationException {
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			throw new BeanInstantiationException(clazz, ex.getMessage(), ex);
		}
	}

}
