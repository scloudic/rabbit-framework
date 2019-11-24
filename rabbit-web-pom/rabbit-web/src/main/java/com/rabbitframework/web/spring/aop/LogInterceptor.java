package com.rabbitframework.web.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 日志拦截接口
 */
public interface LogInterceptor {
    public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable;
}
