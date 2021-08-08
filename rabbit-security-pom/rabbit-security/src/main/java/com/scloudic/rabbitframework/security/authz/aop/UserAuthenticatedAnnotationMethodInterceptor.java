package com.scloudic.rabbitframework.security.authz.aop;

import com.scloudic.rabbitframework.security.authz.handler.UserAuthenticationAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class UserAuthenticatedAnnotationMethodInterceptor extends AuthzAnnotationMethodInterceptor {
    public UserAuthenticatedAnnotationMethodInterceptor() {
        super(new UserAuthenticationAnnotationHandler());
    }

    public UserAuthenticatedAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new UserAuthenticationAnnotationHandler(), resolver);
    }
}
