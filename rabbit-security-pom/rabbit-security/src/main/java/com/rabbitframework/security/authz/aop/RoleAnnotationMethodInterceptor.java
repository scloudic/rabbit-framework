package com.rabbitframework.security.authz.aop;

import com.rabbitframework.security.authz.handler.RoleAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class RoleAnnotationMethodInterceptor extends AuthzAnnotationMethodInterceptor {
    public RoleAnnotationMethodInterceptor() {
        super(new RoleAnnotationHandler());
    }

    public RoleAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new RoleAnnotationHandler(), resolver);
    }
}
