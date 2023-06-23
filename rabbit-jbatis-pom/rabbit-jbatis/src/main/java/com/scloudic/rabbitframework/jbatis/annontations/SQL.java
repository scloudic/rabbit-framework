package com.scloudic.rabbitframework.jbatis.annontations;

import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: juyang.liang
 * @since: 2023-06-23 16:14
 * @updatedUser:
 * @updatedDate:
 * @updatedRemark:
 * @version:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SQL {
    SqlCommendType value() default SqlCommendType.SELECT;
}
