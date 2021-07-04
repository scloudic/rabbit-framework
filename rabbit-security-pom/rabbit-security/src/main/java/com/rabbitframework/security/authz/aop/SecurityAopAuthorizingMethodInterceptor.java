package com.rabbitframework.security.authz.aop;

import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingMethodInterceptor;

import java.util.Collection;

public abstract class SecurityAopAuthorizingMethodInterceptor extends AuthorizingMethodInterceptor {
    protected Collection<AuthzAnnotationMethodInterceptor> methodInterceptors;

    public Collection<AuthzAnnotationMethodInterceptor> getMethodInterceptors() {
        return methodInterceptors;
    }

    public void setMethodInterceptors(Collection<AuthzAnnotationMethodInterceptor> methodInterceptors) {
        this.methodInterceptors = methodInterceptors;
    }
    
    @Override
    protected void assertAuthorized(MethodInvocation methodInvocation) throws AuthorizationException {
        Collection<AuthzAnnotationMethodInterceptor> aamis = getMethodInterceptors();
        if (aamis != null && !aamis.isEmpty()) {
            for (AuthzAnnotationMethodInterceptor aami : aamis) {
                if (aami.supports(methodInvocation)) {
                    aami.assertAuthorized(methodInvocation);
                }
            }
        }
    }
}
