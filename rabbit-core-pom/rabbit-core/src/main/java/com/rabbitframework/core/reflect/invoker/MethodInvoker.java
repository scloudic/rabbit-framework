package com.rabbitframework.core.reflect.invoker;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法映射
 *
 * @author Justin
 */
public class MethodInvoker implements Invoker {

	private Class<?> type;
	private Method method;

	public MethodInvoker(Method method) {
		this.method = method;

		if (method.getParameterTypes().length == 1) {
			type = method.getParameterTypes()[0];
		} else {
			type = method.getReturnType();
		}
	}

	public Object invoke(Object target, Object[] args)
			throws IllegalAccessException, InvocationTargetException {
		return method.invoke(target, args);
	}

	public Class<?> getType() {
		return type;
	}
}
