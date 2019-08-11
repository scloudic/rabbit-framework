package com.rabbitframework.dbase.annontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用于model类，标注数据库字段 规定只用于model的属性标注
 * 
 * @author Justin Liang
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Column {
	String column() default "";

	long length() default 255;

}
