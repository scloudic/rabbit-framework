package com.rabbitframework.security.authz.aop;

import com.rabbitframework.security.authz.handler.NoAccessHandler;
import com.rabbitframework.security.authz.handler.UriPermissionsAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class NoAccessInterceptor extends AuthzAnnotationMethodInterceptor {

    public NoAccessInterceptor() {
        super(new NoAccessHandler());
    }

    public NoAccessInterceptor(AnnotationResolver resolver) {
        super(new NoAccessHandler(), resolver);
    }
}
