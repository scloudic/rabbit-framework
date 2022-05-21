package com.scloudic.rabbitframework.security.web.filter.authz;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 接口不能访问过虑器
 *
 * @author justin
 * @since 3.3.1
 */
public class NoAccessAuthorizationFilter extends PermissionsAuthorizationFilter {
    private static final Logger logger = LoggerFactory.getLogger(NoAccessAuthorizationFilter.class);

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        String path = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
        logger.warn("该请求地址不能访问:" + path);
        return false;
    }
}
