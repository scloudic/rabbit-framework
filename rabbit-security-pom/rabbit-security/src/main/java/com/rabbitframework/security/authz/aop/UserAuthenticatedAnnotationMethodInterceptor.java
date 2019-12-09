package com.rabbitframework.security.authz.aop;

import com.rabbitframework.security.authz.handler.AuthzAnnotationHandler;
import com.rabbitframework.security.authz.handler.UserAuthenticationAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class UserAuthenticatedAnnotationMethodInterceptor extends AuthzAnnotationMethodInterceptor {
    public UserAuthenticatedAnnotationMethodInterceptor() {
        super(new UserAuthenticationAnnotationHandler());
    }

    public UserAuthenticatedAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new UserAuthenticationAnnotationHandler(), resolver);
    }
}
