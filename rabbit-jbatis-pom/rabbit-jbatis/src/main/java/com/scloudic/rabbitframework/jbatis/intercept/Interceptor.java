package com.scloudic.rabbitframework.jbatis.intercept;

/**
 * 拦截器接口类
 * 
 *
 */
public interface Interceptor {
	/**
	 * 拦截方法
	 * 
	 * @param invocation invocation
	 * @return Object
	 * @throws Throwable Throwable
	 */
	public Object intercept(Invocation invocation) throws Throwable;

	/**
	 * 获取拦截器，主要通过plugin映射的机制实现操作
	 * 
	 * @param target target
	 * @return Object
	 */
	public Object plugin(Object target);
}
