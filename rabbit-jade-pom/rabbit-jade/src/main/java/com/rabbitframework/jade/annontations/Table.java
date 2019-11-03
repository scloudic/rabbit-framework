package com.rabbitframework.jade.annontations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Table {
	String name() default "";
}
