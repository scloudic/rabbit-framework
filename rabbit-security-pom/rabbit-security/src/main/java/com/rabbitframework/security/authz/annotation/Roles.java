package com.rabbitframework.security.authz.annotation;

import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Roles {

    /**
     * A single String role name or multiple comma-delimitted role names required in order for the method
     * invocation to be allowed.
     */
    String[] value();

    /**
     * The logical operation for the permission check in case multiple roles are specified. AND is the default
     *
     * @since 1.1.0
     */
    Logical logical() default Logical.AND;
}
