package com.rabbitframework.security.web.filter.authz;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class NoPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {
    private static final Logger logger = LoggerFactory.getLogger(NoPermissionsAuthorizationFilter.class);

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        String path = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
        logger.warn("该请求地址不能访问:" + path);
        return false;
    }
}
