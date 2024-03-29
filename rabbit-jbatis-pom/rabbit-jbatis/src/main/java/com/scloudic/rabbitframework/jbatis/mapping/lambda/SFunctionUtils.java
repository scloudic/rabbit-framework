package com.scloudic.rabbitframework.jbatis.mapping.lambda;

import com.scloudic.rabbitframework.core.utils.StringUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class SFunctionUtils {
    /**
     * 获取表中字段名称
     * @param fn fn
     * @param <T> T
     * @return string
     */
    public static <T> String getFieldName(SFunction<T, ?> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String getter = serializedLambda.getImplMethodName();
            String fieldName = Introspector.decapitalize(getter.replace("get", ""));
            return StringUtils.toUnderScoreCase(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取model对象属性名称
     * @param fn fn
     * @param <T> t
     * @return string
     */
    public static <T> String getFieldPropertyName(SFunction<T, ?> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String getter = serializedLambda.getImplMethodName();
            String fieldName = Introspector.decapitalize(getter.replace("get", ""));
            return fieldName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
