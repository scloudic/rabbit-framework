package com.rabbitframework.core.reflect.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * 映射接口
 *
 * @author Justin
 *
 */
public interface Invoker {
	Object invoke(Object target, Object[] args) throws IllegalAccessException,
			InvocationTargetException;

	Class<?> getType();
}
