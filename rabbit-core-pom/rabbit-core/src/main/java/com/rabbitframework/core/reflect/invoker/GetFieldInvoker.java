package com.rabbitframework.core.reflect.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * get字段映射
 *
 *
 */
public class GetFieldInvoker implements Invoker {
	private Field field;

	public GetFieldInvoker(Field field) {
		this.field = field;
	}

	public Object invoke(Object target, Object[] args)
			throws IllegalAccessException, InvocationTargetException {
		return field.get(target);
	}

	public Class<?> getType() {
		return field.getType();
	}
}
