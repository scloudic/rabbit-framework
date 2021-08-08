package com.scloudic.rabbitframework.core.reflect.factory;

import java.util.List;
import java.util.Properties;

/**
 * 使用object创建新的对象
 *
 */
public interface ObjectFactory {
	/**
	 * 设置初始化属性
	 *
	 * @param properties
	 */
	void setProperties(Properties properties);

	/**
	 * 创建默认构造函数对象
	 *
	 * @param type
	 * @return
	 */
	<T> T create(Class<T> type);

	/**
	 * 创建带参数的构造函数对象
	 *
	 * @param type
	 * @param constructorArgTypes
	 * @param constructorArgs
	 * @return
	 */
	<T> T create(Class<T> type, List<Class<?>> constructorArgTypes,
                 List<Object> constructorArgs);

	/**
	 *
	 * 是否为Collection
	 *
	 * @param type
	 * @return
	 */
	<T> boolean isCollection(Class<T> type);

}
