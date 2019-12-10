package com.rabbitframework.security.web.filter.authz;

import com.tjzq.commons.utils.JsonUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class SecurityAuthorizationFilter extends AuthorizationFilter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthorizationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setContentType("text/json; charset=utf-8");
        if (subject.getPrincipal() == null) {
            String loginUrl = getLoginUrl();
            if (StringUtils.hasText(loginUrl) && (!AccessControlFilter.DEFAULT_LOGIN_URL.equals(loginUrl))) {
                saveRequestAndRedirectToLogin(request, response);
            } else {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("status", HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
                json.put("message", "Authentication fail");
                httpServletResponse.sendError(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED, JsonUtils.toJsonString(json));
            }
        } else {
            String unauthorizedUrl = getUnauthorizedUrl();
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                json.put("message", "Authorization fail");
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, JsonUtils.toJsonString(json));
            }
        }
        return false;
    }
}
