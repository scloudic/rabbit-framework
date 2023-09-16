package com.scloudic.rabbitframework.redisson.annotations;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockValue {
    boolean isObject() default false;

    String keyName() default "";
}
