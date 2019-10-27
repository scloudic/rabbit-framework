package com.rabbitframework.commons;

import com.tjzq.commons.org.springframework.util.Assert;

/**
 * 继承 {@link ThreadLocal}
 * 
 * @author: justin.liang
 * @date: 16/5/21 下午11:10
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {
	private final String name;

	public NamedThreadLocal(String name) {
		Assert.hasText(name, "Name must not be empty");
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
}
