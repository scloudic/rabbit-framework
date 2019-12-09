package com.rabbitframework.security.authz.annotation;

import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权色拦截注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Roles {

    String[] value();

    Logical logical() default Logical.AND;
}
