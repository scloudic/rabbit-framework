package com.rabbitframework.security.authz.handler;

import com.rabbitframework.security.authz.annotation.UserAuthentication;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

public class UserAuthenticationAnnotationHandler extends AuthzAnnotationHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationAnnotationHandler.class);

    public UserAuthenticationAnnotationHandler() {
        super(UserAuthentication.class);
    }

    @Override
    public void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException {
        if (a instanceof UserAuthentication && !getSubject().isAuthenticated()) {
            logger.warn("The current Subject is not authenticated.  Access denied.");
            throw new UnauthenticatedException("authc.fail");
        }
    }
}
