package com.rabbitframework.security.authz.handler;

import com.rabbitframework.security.authz.annotation.UserAuthentication;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;

import java.lang.annotation.Annotation;

public class UserAuthenticationAnnotationHandler extends AuthzAnnotationHandler {
    public UserAuthenticationAnnotationHandler() {
        super(UserAuthentication.class);
    }

    @Override
    public void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException {
        if (a instanceof UserAuthentication && !getSubject().isAuthenticated()) {
            throw new AuthorizationException("The current Subject is not authenticated.  Access denied.");
        }
    }
}
