package com.scloudic.rabbitframework.security.spring.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * spring注解拦截获取注解信息
 *
 * @author justin
 * @since 3.3.1
 */
public class SpringAnnotationResolver implements AnnotationResolver {
    /**
     * Returns an {@link Annotation} instance of the specified type based on the given
     * {@link MethodInvocation MethodInvocation} argument, or {@code null} if no annotation
     * of that type could be found. First checks the invoked method itself and if not found,
     * then the class for the existence of the same annotation.
     *
     * @param mi    the intercepted method to be invoked.
     * @param clazz the annotation class of the annotation to find.
     * @return the method's annotation of the specified type or {@code null} if no annotation of
     * that type could be found.
     */
    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        Method m = mi.getMethod();
        Annotation a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null)
            return a;
        Class<?> targetClass = mi.getThis().getClass();
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null)
            return a;
        return AnnotationUtils.findAnnotation(mi.getThis().getClass(), clazz);
    }
}
