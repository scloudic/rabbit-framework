package com.rabbitframework.security.web.filter.authz;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.rabbitframework.core.utils.StatusCode;
import com.rabbitframework.security.web.filter.RedirectUtils;
import org.apache.shiro.subject.Subject;

/**
 * 实现roles[admin,test]或的关系
 *
 * @author justin
 * @UpdateVersion 3.3.1
 */
public class RolesOrAuthorizationFilter extends RolesAuthzFilter {
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        for (int i = 0; i < rolesArray.length; i++) {
            if (subject.hasRole(rolesArray[i])) {
                return true;
            }
        }
        return false;
    }

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
