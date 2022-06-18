package com.scloudic.rabbitframework.core.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {
    String key();

    long waitTime() default 10L;

    long leaseTime() default -1L;

    String exceptionMsg() default "lock.fail";

}
