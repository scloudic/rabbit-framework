package com.rabbitframework.jade.mapping.binding;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.rabbitframework.jade.dataaccess.SqlDataAccess;

/**
 * mapper代理类
 *
 * @author Justin Liang
 *
 *
 */
public class MapperProxy<T> implements InvocationHandler, java.io.Serializable {
	private static final long serialVersionUID = -2978351994210237954L;
	private final SqlDataAccess sqlDataAccess;
	private final Map<Method, MapperMethod> methodCache;
	private final Class<T> mapperInterface;

	public MapperProxy(SqlDataAccess sqlDataAccess,
			Map<Method, MapperMethod> methodCache, Class<T> mapperInterface) {
		this.sqlDataAccess = sqlDataAccess;
		this.methodCache = methodCache;
		this.mapperInterface = mapperInterface;
	}

	/**
	 * 执行mapper接口方法,调用{@link MapperMethod}的execute执行
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		final MapperMethod mapperMethod = cachedMapperMethod(method);
		return mapperMethod.execute(sqlDataAccess, args);
	}

	private MapperMethod cachedMapperMethod(Method method) {
		MapperMethod mapperMethod = methodCache.get(method);
		if (mapperMethod == null) {
			mapperMethod = new MapperMethod(mapperInterface, method,
					sqlDataAccess.getConfiguration());
			methodCache.put(method, mapperMethod);
		}
		return mapperMethod;
	}
}
