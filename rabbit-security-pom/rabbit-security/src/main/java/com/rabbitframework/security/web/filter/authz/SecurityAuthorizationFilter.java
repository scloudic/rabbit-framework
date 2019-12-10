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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class SecurityAuthorizationFilter extends AuthorizationFilter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthorizationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        PrintWriter printWriter = null;
        try {
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
                    json.put("message", "authc.fail");
                    printWriter = httpServletResponse.getWriter();
                    printWriter.write(JsonUtils.toJsonString(json));
                }
            } else {
                String unauthorizedUrl = getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    Map<String, Object> json = new HashMap<String, Object>();
                    json.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                    json.put("message", "authz.fail");
                    printWriter = httpServletResponse.getWriter();
                    printWriter.write(JsonUtils.toJsonString(json));
                }
            }
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                response.flushBuffer();
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return false;
    }
}
