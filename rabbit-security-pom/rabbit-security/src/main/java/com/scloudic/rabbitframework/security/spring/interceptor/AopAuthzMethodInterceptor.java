package com.scloudic.rabbitframework.security.spring.interceptor;

import com.scloudic.rabbitframework.security.authz.aop.*;
import com.scloudic.rabbitframework.security.spring.aop.SpringAnnotationResolver;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.aop.AnnotationResolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * aop权限拦截器
 */
public class AopAuthzMethodInterceptor
        extends SecurityAopAuthorizingMethodInterceptor implements MethodInterceptor {

    public AopAuthzMethodInterceptor() {
        List<AuthzAnnotationMethodInterceptor> interceptors = new ArrayList<AuthzAnnotationMethodInterceptor>();
        AnnotationResolver resolver = new SpringAnnotationResolver();
        interceptors.add(new RoleAnnotationMethodInterceptor(resolver));
        interceptors.add(new UriPermissionsAnnotationMethodInterceptor());
        interceptors.add(new PermissionAnnotationMethodInterceptor(resolver));
        interceptors.add(new UserAuthenticatedAnnotationMethodInterceptor(resolver));
        interceptors.add(new NoAccessInterceptor(resolver));
        setMethodInterceptors(interceptors);
    }

    protected org.apache.shiro.aop.MethodInvocation createMethodInvocation(Object implSpecificMethodInvocation) {
        final MethodInvocation mi = (MethodInvocation) implSpecificMethodInvocation;

        return new org.apache.shiro.aop.MethodInvocation() {
            public Method getMethod() {
                return mi.getMethod();
            }

            public Object[] getArguments() {
                return mi.getArguments();
            }

            public String toString() {
                return "Method invocation [" + mi.getMethod() + "]";
            }

            public Object proceed() throws Throwable {
                return mi.proceed();
            }

            public Object getThis() {
                return mi.getThis();
            }
        };
    }

    protected Object continueInvocation(Object aopAllianceMethodInvocation) throws Throwable {
        MethodInvocation mi = (MethodInvocation) aopAllianceMethodInvocation;
        return mi.proceed();
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        org.apache.shiro.aop.MethodInvocation mi = createMethodInvocation(methodInvocation);
        return super.invoke(mi);
    }
}
