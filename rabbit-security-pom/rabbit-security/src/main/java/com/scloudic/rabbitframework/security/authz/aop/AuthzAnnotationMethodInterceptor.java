package com.scloudic.rabbitframework.security.authz.aop;

import com.scloudic.rabbitframework.core.exceptions.AuthcException;
import com.scloudic.rabbitframework.core.exceptions.AuthzException;
import com.scloudic.rabbitframework.security.authz.handler.AuthzAnnotationHandler;
import org.apache.shiro.aop.AnnotationMethodInterceptor;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
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
            logger.error(ae.getMessage() + ",Not authorized to invoke method: " + mi.getMethod());
            if (ae instanceof UnauthenticatedException) {
                throw new AuthcException("authc.fail");
            } else {
                throw new AuthzException("authz.fail");
            }
        }
    }
}
