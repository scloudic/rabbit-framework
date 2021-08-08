package com.scloudic.rabbitframework.web.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FormValid {
	/**
	 * 字段过滤,过滤不要验证的字段
	 * 
	 * @return string
	 */
	String[] fieldFilter() default {};
}
