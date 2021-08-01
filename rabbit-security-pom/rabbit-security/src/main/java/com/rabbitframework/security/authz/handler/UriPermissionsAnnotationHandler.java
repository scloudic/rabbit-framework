package com.rabbitframework.security.authz.handler;

import com.rabbitframework.security.authz.annotation.UriPermissions;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.lang.annotation.Annotation;

/**
 * uri权限处理类
 */
public class UriPermissionsAnnotationHandler extends AuthzAnnotationHandler {
    private static final Logger logger = LoggerFactory.getLogger(UriPermissionsAnnotationHandler.class);

    public UriPermissionsAnnotationHandler() {
        super(UriPermissions.class);
    }

    @Override
    public void assertAuthorized(Annotation a, MethodInvocation mi) throws AuthorizationException {
        Object[] objects = mi.getArguments();
        ServletRequest request = null;
        for (Object object : objects) {
            if (object instanceof ServletRequest) {
                request = (ServletRequest) object;
                break;
            }
        }
        if (request == null) {
            logger.warn("request is null");
            throw new AuthorizationException("request is null");
        }
        String requestUri = getPathWithinApplication(request);
        if (logger.isDebugEnabled()) {
            logger.debug("requestUrl:" + requestUri);
        }
        Subject subject = getSubject();
        //优先判断权限
        if (!subject.isAuthenticated()) {
            throw new UnauthenticatedException("authc.fail");
        }
        subject.checkPermission(requestUri);
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }
}
