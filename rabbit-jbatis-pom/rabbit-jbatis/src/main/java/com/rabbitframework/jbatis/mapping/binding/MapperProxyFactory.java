package com.rabbitframework.jbatis.mapping.binding;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rabbitframework.jbatis.dataaccess.SqlDataAccess;

/**
 * {@link MapperProxy}代理工厂
 *
 * @author Justin Liang
 *
 *
 */
public class MapperProxyFactory<T> {
	private final Class<T> mapperInterface;
	private Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

	public MapperProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public Map<Method, MapperMethod> getMethodCache() {
		return methodCache;
	}

	@SuppressWarnings("unchecked")
	protected T newInstance(MapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
				new Class[] { mapperInterface }, mapperProxy);
	}

	public T newInstance(SqlDataAccess sqlDataAccess) {
		final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlDataAccess,
				methodCache, mapperInterface);
		return newInstance(mapperProxy);
	}
}
