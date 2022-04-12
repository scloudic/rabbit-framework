package com.scloudic.rabbitframework.core.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {
    String key();

    long seconds() default 10L;

    String exceptionMsg() default "lock.fail";

}
