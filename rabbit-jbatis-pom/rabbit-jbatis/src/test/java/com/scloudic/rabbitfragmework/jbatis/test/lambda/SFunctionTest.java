package com.scloudic.rabbitfragmework.jbatis.test.lambda;

import com.scloudic.rabbitfragmework.jbatis.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.mapping.lambda.SFunction;
import com.scloudic.rabbitframework.core.utils.StringUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class SFunctionTest {
    public static void main(String[] args) {
        System.out.println(getName(TestUser::getId));
    }

    private static <T> String getName(SFunction<T, ?> fn) {
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
}
