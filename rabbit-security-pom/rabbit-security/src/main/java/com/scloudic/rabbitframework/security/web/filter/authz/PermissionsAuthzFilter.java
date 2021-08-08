package com.scloudic.rabbitframework.security.web.filter.authz;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.security.web.filter.RedirectUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;

/**
 * 权限安全过滤器，实现{@link PermissionsAuthorizationFilter#onAccessDenied(ServletRequest, ServletResponse)} 方法
 *
 * @since 3.3.1
 */
public class PermissionsAuthzFilter extends PermissionsAuthorizationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            String loginUrl = getLoginUrl();
            RedirectUtils.redirect(request, response, loginUrl, StatusCode.SC_PROXY_AUTHENTICATION_REQUIRED);
        } else {
            String unauthorizedUrl = getUnauthorizedUrl();
            RedirectUtils.redirect(request, response, unauthorizedUrl, StatusCode.SC_UNAUTHORIZED);
        }
        return false;
    }
}
