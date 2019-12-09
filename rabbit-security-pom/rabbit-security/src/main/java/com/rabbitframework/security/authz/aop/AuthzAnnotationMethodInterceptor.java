package com.rabbitframework.security.authz.aop;

import com.rabbitframework.commons.exceptions.AuthzException;
import com.rabbitframework.security.authz.handler.AuthzAnnotationHandler;
import org.apache.shiro.aop.AnnotationMethodInterceptor;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthzAnnotationMethodInterceptor extends AnnotationMethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthzAnnotationMethodInterceptor.class);

    public AuthzAnnotationMethodInterceptor(AuthzAnnotationHandler handler) {
        super(handler);
    }


    public AuthzAnnotationMethodInterceptor(AuthzAnnotationHandler handler,
                                            AnnotationResolver resolver) {
        super(handler, resolver);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        assertAuthorized(methodInvocation);
        return methodInvocation.proceed();
    }

    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            ((AuthzAnnotationHandler) getHandler()).assertAuthorized(getAnnotation(mi), mi);
        } catch (Exception ae) {
            logger.warn(ae.getMessage() + ",Not authorized to invoke method: " + mi.getMethod());
            throw new AuthorizationException(new AuthzException("authz.error"));
        }
    }
}
