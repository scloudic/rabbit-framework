package com.rabbitframework.dbase.annontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rabbitframework.dbase.mapping.GenerationType;

/**
 * 数据库实体映射，标注数据库主键
 * 
 * @author Justin Liang
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ID {
	String column() default "";

	long length() default 255;

	GenerationType keyType() default GenerationType.IDENTITY;

	String selectKey() default "";

}
