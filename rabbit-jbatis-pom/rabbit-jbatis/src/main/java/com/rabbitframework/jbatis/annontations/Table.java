package com.rabbitframework.jbatis.annontations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Table {
	String name() default "";
}
