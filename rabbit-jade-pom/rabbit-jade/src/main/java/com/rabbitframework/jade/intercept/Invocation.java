package com.rabbitframework.jade.intercept;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation {
	private Object target;
	private Method method;
	private Object[] args;

	public Invocation(Object target, Method method, Object[] args) {
		super();
		this.target = target;
		this.method = method;
		this.args = args;
	}

	public Object getTarget() {
		return target;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

	public Object process() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return method.invoke(target, args);
	}
}
