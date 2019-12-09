package com.rabbitframework.security.authz.handler;

import com.rabbitframework.security.authz.annotation.Roles;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class RoleAnnotationHandler extends AuthzAnnotationHandler {

    public RoleAnnotationHandler() {
        super(Roles.class);
    }

    @Override
    public void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException {
        if (!(a instanceof Roles))
            return;

        Roles rrAnnotation = (Roles) a;
        String[] roles = rrAnnotation.value();

        if (roles.length == 1) {
            getSubject().checkRole(roles[0]);
            return;
        }
        if (Logical.AND.equals(rrAnnotation.logical())) {
            getSubject().checkRoles(Arrays.asList(roles));
            return;
        }
        if (Logical.OR.equals(rrAnnotation.logical())) {
            boolean hasAtLeastOneRole = false;
            for (String role : roles) if (getSubject().hasRole(role)) hasAtLeastOneRole = true;
            if (!hasAtLeastOneRole) getSubject().checkRole(roles[0]);
        }
    }
}
