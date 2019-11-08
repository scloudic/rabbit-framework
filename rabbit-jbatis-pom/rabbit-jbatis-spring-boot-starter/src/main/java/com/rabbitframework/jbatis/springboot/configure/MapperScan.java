package com.rabbitframework.jbatis.springboot.configure;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MapperScan {

    @AliasFor("basePackages")
    String value() default "";

    @AliasFor("value")
    String basePackages() default "";
}
