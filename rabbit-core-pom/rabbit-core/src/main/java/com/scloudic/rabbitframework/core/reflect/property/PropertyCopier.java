package com.scloudic.rabbitframework.core.reflect.property;

import java.lang.reflect.Field;

/**
 * 对象复制
 *
 * @author Justin
 */
public class PropertyCopier {

	public static void copyBeanProperties(Class<?> type, Object sourceBean,
			Object destinationBean) {
		Class<?> parent = type;
		while (parent != null) {
			final Field[] fields = parent.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					field.set(destinationBean, field.get(sourceBean));
				} catch (Exception e) {
					// Nothing useful to do, will only fail on final fields,
					// which will be ignored.
				}
			}
			parent = parent.getSuperclass();
		}
	}

}
