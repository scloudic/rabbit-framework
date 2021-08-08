package com.scloudic.rabbitframework.web.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 定义模板注解
 *
 * @author justin
 * @since 3.3.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TemplateVariable {
    String value();
}
