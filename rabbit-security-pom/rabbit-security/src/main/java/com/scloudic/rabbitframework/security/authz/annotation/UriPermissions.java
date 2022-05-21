package com.scloudic.rabbitframework.security.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * uri权限控制,优先判断登陆拦截
 *
 * @author justin.liang
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UriPermissions {
}
