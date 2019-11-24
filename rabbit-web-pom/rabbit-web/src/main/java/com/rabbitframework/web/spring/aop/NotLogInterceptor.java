package com.rabbitframework.web.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空日志拦截类
 *
 * @author: justin
 * @date: 2017-06-13 12:13
 */

public class NotLogInterceptor implements LogInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(NotLogInterceptor.class);

    public NotLogInterceptor() {
        logger.debug("NotLogInterceptor start");
    }

	@Override
	public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable {
		return null;
	}
}
