package com.scloudic.rabbitframework.security.authz.handler;

import com.scloudic.rabbitframework.security.authz.annotation.UriPermissions;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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
        HttpServletRequest request = null;
        request = (HttpServletRequest) RequestContextHolder.currentRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (request == null) {
            for (Object object : objects) {
                if (object instanceof HttpServletRequest) {
                    request = (HttpServletRequest) object;
                    break;
                }
            }
        }
        if (request == null) {
            logger.error("uri权限制获取request失败");
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
