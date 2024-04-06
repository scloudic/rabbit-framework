package com.scloudic.rabbitframework.security.web.filter.authz;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.security.web.filter.RabbitSecurityFilter;
import com.scloudic.rabbitframework.security.web.filter.RedirectUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 角色安全过滤,实现{@link RolesAuthorizationFilter#onAccessDenied(ServletRequest, ServletResponse)}
 *
 * @since 3.3.1
 */
public class RolesAuthzFilter extends RolesAuthorizationFilter implements RabbitSecurityFilter {
    private boolean frontEndSeparate = true;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            String loginUrl = getLoginUrl();
            RedirectUtils.redirect(request, response, loginUrl, StatusCode.SC_PROXY_AUTHENTICATION_REQUIRED,frontEndSeparate);
        } else {
            String unauthorizedUrl = getUnauthorizedUrl();
            RedirectUtils.redirect(request, response, unauthorizedUrl, StatusCode.SC_UNAUTHORIZED,frontEndSeparate);
        }
        return false;
    }

    @Override
    public void setFrontEndSeparate(boolean frontEndSeparate) {
        this.frontEndSeparate = frontEndSeparate;
    }
}
