package com.scloudic.rabbitframework.security.authz.aop;

import com.scloudic.rabbitframework.security.authz.handler.UriPermissionsAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;

public class UriPermissionsAnnotationMethodInterceptor extends AuthzAnnotationMethodInterceptor {

    public UriPermissionsAnnotationMethodInterceptor() {
        super(new UriPermissionsAnnotationHandler());
    }

    public UriPermissionsAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new UriPermissionsAnnotationHandler(), resolver);
    }
}
