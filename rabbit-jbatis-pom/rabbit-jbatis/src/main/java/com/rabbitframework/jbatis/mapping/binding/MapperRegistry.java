package com.rabbitframework.jbatis.mapping.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.builder.MapperParser;
import com.rabbitframework.jbatis.dataaccess.SqlDataAccess;
import com.rabbitframework.jbatis.exceptions.BindingException;

public class MapperRegistry {
	private Configuration configuration;
	private final Map<Class<?>, MapperProxyFactory<?>> mappers = new HashMap<Class<?>, MapperProxyFactory<?>>();

	public MapperRegistry(Configuration configuration) {
		this.configuration = configuration;
	}

	public <T> boolean hasMapper(Class<T> mapperInterface) {
		return mappers.containsKey(mapperInterface);
	}

	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> mapperInterface, SqlDataAccess sqlDataAccess) {
		final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) mappers
				.get(mapperInterface);
		if (mapperProxyFactory == null)
			throw new BindingException("mapperInterface " + mapperInterface
					+ " is not known to the MapperRegistry.");
		try {
			return mapperProxyFactory.newInstance(sqlDataAccess);
		} catch (Exception e) {
			throw new BindingException("Error getting mapper instance.Cause: "
					+ e);
		}
	}

	public <T> void addMapper(Class<T> mapperInterface) {
		// 判断是否为接口类
		if (mapperInterface.isInterface()) {
			if (hasMapper(mapperInterface)) {
				throw new BindingException("Type " + mapperInterface
						+ "is already known to the MapperRegistry.");
			}
			boolean loadCompleted = false;
			try {
				mappers.put(mapperInterface, new MapperProxyFactory<T>(
						mapperInterface));
				MapperParser parser = new MapperParser(configuration,
						mapperInterface);
				parser.parse();
				loadCompleted = true;
			} finally {
				if (!loadCompleted) {
					mappers.remove(mapperInterface);
				}
			}
		}
	}

	public Collection<Class<?>> getMappers() {
		return Collections.unmodifiableCollection(mappers.keySet());
	}
}
