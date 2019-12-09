package com.rabbitframework.security.authz.handler;

import org.apache.shiro.aop.AnnotationHandler;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;

import java.lang.annotation.Annotation;

public abstract class AuthzAnnotationHandler extends AnnotationHandler {

    public AuthzAnnotationHandler(Class<? extends Annotation> annotationClass) {
        super(annotationClass);
    }

    public abstract void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException;
}
