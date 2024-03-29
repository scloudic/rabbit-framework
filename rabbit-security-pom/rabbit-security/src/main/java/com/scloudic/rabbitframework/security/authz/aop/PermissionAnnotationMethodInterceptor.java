package com.scloudic.rabbitframework.security.authz.aop;

import com.scloudic.rabbitframework.security.authz.handler.PermissionAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class PermissionAnnotationMethodInterceptor extends AuthzAnnotationMethodInterceptor {
    public PermissionAnnotationMethodInterceptor() {
        super(new PermissionAnnotationHandler());
    }

    public PermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new PermissionAnnotationHandler(), resolver);
    }
}
