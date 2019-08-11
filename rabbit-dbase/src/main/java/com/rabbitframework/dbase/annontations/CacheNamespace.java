package com.rabbitframework.dbase.annontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 缓存设置 如果是查询语句时，将进行缓存操作,其它将视为清除缓存操作
 * </p>
 * 缓存key在做缓存处理时，只取第一条记录值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheNamespace {
	String pool();

	String[] key();
}
