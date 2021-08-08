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
	 * @param properties properties
	 */
	void setProperties(Properties properties);

	/**
	 * 创建默认构造函数对象
	 * @param type type
	 * @param <T> T
	 * @return t
	 */
	<T> T create(Class<T> type);

	/**
	 * 创建带参数的构造函数对象
	 * @param type type
	 * @param constructorArgTypes constructorArgTypes
	 * @param constructorArgs constructorArgs
	 * @param <T> t
	 * @return t
	 */
	<T> T create(Class<T> type, List<Class<?>> constructorArgTypes,
                 List<Object> constructorArgs);

	/**
	 * 是否为Collection
	 * @param type type
	 * @param <T> t
	 * @return boolean
	 */
	<T> boolean isCollection(Class<T> type);

}
