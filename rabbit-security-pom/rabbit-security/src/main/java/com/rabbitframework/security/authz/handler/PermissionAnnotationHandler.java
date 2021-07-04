package com.rabbitframework.security.authz.handler;

import com.rabbitframework.security.authz.annotation.Permissions;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

public class PermissionAnnotationHandler extends AuthzAnnotationHandler {
    public PermissionAnnotationHandler() {
        super(Permissions.class);
    }

    protected String[] getAnnotationValue(Annotation a) {
        Permissions rpAnnotation = (Permissions) a;
        return rpAnnotation.value();
    }


    @Override
    public void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException {
        if (!(a instanceof Permissions)) return;

        Permissions rpAnnotation = (Permissions) a;
        String[] perms = getAnnotationValue(a);
        Subject subject = getSubject();
        //优先判断是否登录
        if (!subject.isAuthenticated()) {
            throw new UnauthenticatedException("authc.fail");
        }
        if (perms.length == 1) {
            subject.checkPermission(perms[0]);
            return;
        }
        if (Logical.AND.equals(rpAnnotation.logical())) {
            getSubject().checkPermissions(perms);
            return;
        }
        if (Logical.OR.equals(rpAnnotation.logical())) {
            boolean hasAtLeastOnePermission = false;
            for (String permission : perms) {
                if (getSubject().isPermitted(permission)) {
                    hasAtLeastOnePermission = true;
                }
            }
            if (!hasAtLeastOnePermission) getSubject().checkPermission(perms[0]);

        }
    }
}
