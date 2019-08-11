package com.rabbitframework.security.spring;

/**
 * spring 生命周期
 *
 * @author: justin
 * @date: 2019-06-29 11:17
 */
public class LifecycleBeanPostProcessor extends org.apache.shiro.spring.LifecycleBeanPostProcessor {
    /**
     * Default Constructor.
     */
    public LifecycleBeanPostProcessor() {
        this(LOWEST_PRECEDENCE);
    }

    public LifecycleBeanPostProcessor(int order) {
        super(order);
    }
}
