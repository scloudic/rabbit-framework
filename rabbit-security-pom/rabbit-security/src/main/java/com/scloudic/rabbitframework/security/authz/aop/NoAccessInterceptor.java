package com.scloudic.rabbitframework.security.authz.aop;

import com.scloudic.rabbitframework.security.authz.handler.NoAccessHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class NoAccessInterceptor extends AuthzAnnotationMethodInterceptor {

    public NoAccessInterceptor() {
        super(new NoAccessHandler());
    }

    public NoAccessInterceptor(AnnotationResolver resolver) {
        super(new NoAccessHandler(), resolver);
    }
}
