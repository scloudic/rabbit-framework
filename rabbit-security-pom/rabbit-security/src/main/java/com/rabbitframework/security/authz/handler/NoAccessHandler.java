package com.rabbitframework.security.authz.handler;

import com.rabbitframework.commons.exceptions.AuthcException;
import com.rabbitframework.security.authz.annotation.NoAccess;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.lang.annotation.Annotation;

/**
 * 不可访问的接口拦截
 */
public class NoAccessHandler extends AuthzAnnotationHandler {
    public NoAccessHandler() {
        super(NoAccess.class);
    }

    @Override
    public void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException {
        throw new AuthorizationException(new AuthcException("authc.fail"));
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }
}
