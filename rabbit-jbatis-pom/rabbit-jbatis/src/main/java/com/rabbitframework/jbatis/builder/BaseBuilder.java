package com.rabbitframework.jbatis.builder;

import com.rabbitframework.core.utils.ClassUtils;

public abstract class BaseBuilder {
	protected final Configuration configuration;

	public BaseBuilder(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	protected Class<?> resolveClass(String alias) {
		return ClassUtils.classForName(alias);
	}
}
