package com.rabbitframework.commons;

import com.tjzq.commons.org.springframework.util.Assert;

/**
 * 继承 {@link InheritableThreadLocal}
 *
 * @author: justin.liang
 * @date: 16/5/21 下午11:11
 */
public class NamedInheritableThreadLocal<T> extends InheritableThreadLocal<T> {
    private final String name;

    public NamedInheritableThreadLocal(String name) {
        Assert.hasText(name, "Name must not be empty");
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}