package com.rabbitframework.core.reflect.factory;

import com.rabbitframework.core.exceptions.ReflectionException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

public class DefaultObjectFactory implements ObjectFactory, Serializable {
	private static final long serialVersionUID = -6342219900307121045L;

	@Override
	public void setProperties(Properties properties) {

	}

	@Override
	public <T> T create(Class<T> type) {
		return create(type, null, null);
	}

	@Override
	public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes,
			List<Object> constructorArgs) {
		Class<?> classToCreate = resolveInterface(type);
		@SuppressWarnings("unchecked")
		T created = (T) instantiateClass(classToCreate, constructorArgTypes,
				constructorArgs);
		return created;
	}

	private <T> T instantiateClass(Class<T> type,
			List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
		try {
			Constructor<T> constructor;
			if (constructorArgTypes == null || constructorArgs == null) {
				constructor = type.getDeclaredConstructor();
				if (!constructor.isAccessible()) {
					constructor.setAccessible(true);
				}
				return constructor.newInstance();
			}
			constructor = type.getDeclaredConstructor(constructorArgTypes
					.toArray(new Class[constructorArgTypes.size()]));
			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			return constructor.newInstance(constructorArgs
					.toArray(new Object[constructorArgs.size()]));
		} catch (Exception e) {
			StringBuilder argTypes = new StringBuilder();
			if (constructorArgTypes != null) {
				for (Class<?> argType : constructorArgTypes) {
					argTypes.append(argType.getSimpleName());
					argTypes.append(",");
				}
			}
			StringBuilder argValues = new StringBuilder();
			if (constructorArgs != null) {
				for (Object argValue : constructorArgs) {
					argValues.append(String.valueOf(argValue));
					argValues.append(",");
				}
			}
			throw new ReflectionException("Error instantiating " + type
					+ " with invalid types (" + argTypes + ") or values ("
					+ argValues + "). Cause: " + e, e);
		}
	}

	/**
	 * 是否为集合接口并获取接口类
	 *
	 * @param type
	 * @return
	 */
	protected Class<?> resolveInterface(Class<?> type) {
		Class<?> classToCreate;
		if (type == List.class || type == Collection.class
				|| type == Iterable.class) {
			classToCreate = ArrayList.class;
		} else if (type == Map.class) {
			classToCreate = HashMap.class;
		} else if (type == SortedSet.class) { // issue #510 Collections Support
			classToCreate = TreeSet.class;
		} else if (type == Set.class) {
			classToCreate = HashSet.class;
		} else {
			classToCreate = type;
		}
		return classToCreate;
	}

	@Override
	public <T> boolean isCollection(Class<T> type) {
		return Collection.class.isAssignableFrom(type);
	}

}
