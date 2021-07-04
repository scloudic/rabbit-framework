package com.rabbitframework.security.web.filter.authz;

import com.rabbitframework.core.utils.StatusCode;
import com.rabbitframework.security.web.filter.RedirectUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 权限过虑器
 *
 * @author: justin
 * @date: 2018-04-21 下午11:46
 */
public class UriPermissionsFilter extends PermissionsAuthorizationFilter {
    private static final Logger logger = LoggerFactory.getLogger(UriPermissionsFilter.class);

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        String requestUri = getPathWithinApplication(request);
        if (logger.isDebugEnabled()) {
            logger.debug("requestUrl:" + requestUri);
        }
        boolean result = super.isAccessAllowed(request, response, new String[]{requestUri});
        return result;
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
