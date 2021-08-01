package com.rabbitframework.security.web.filter.mgt;

import com.rabbitframework.security.web.filter.authc.FormAuthcFilter;
import com.rabbitframework.security.web.filter.authz.*;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.filter.InvalidRequestFilter;
import org.apache.shiro.web.filter.authc.*;
import org.apache.shiro.web.filter.authz.*;
import org.apache.shiro.web.filter.session.NoSessionCreationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.LinkedHashMap;
import java.util.Map;

public enum SecurityFilter {
    noAccess(NoAccessAuthorizationFilter.class),
    rolesOr(RolesOrAuthorizationFilter.class),
    uriPerms(UriPermissionsFilter.class),
    anon(AnonymousFilter.class),
    authc(FormAuthcFilter.class),
    authcBasic(BasicHttpAuthenticationFilter.class),
    authcBearer(BearerHttpAuthenticationFilter.class),
    logout(LogoutFilter.class),
    noSessionCreation(NoSessionCreationFilter.class),
    perms(PermissionsAuthzFilter.class),
    port(PortFilter.class),
    rest(HttpMethodPermissionFilter.class),
    roles(RolesAuthzFilter.class),
    ssl(SslFilter.class),
    user(UserFilter.class),
    invalidRequest(InvalidRequestFilter.class);
    private final Class<? extends Filter> filterClass;

    private SecurityFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    public Filter newInstance() {
        return (Filter) ClassUtils.newInstance(this.filterClass);
    }

    public Class<? extends Filter> getFilterClass() {
        return this.filterClass;
    }

    public static Map<String, Filter> createInstanceMap(FilterConfig config) {
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>(values().length);
        for (SecurityFilter defaultFilter : values()) {
            Filter filter = defaultFilter.newInstance();
            if (config != null) {
                try {
                    filter.init(config);
                } catch (ServletException e) {
                    String msg = "Unable to correctly init default filter instance of type "
                            + filter.getClass().getName();
                    throw new IllegalStateException(msg, e);
                }
            }
            filters.put(defaultFilter.name(), filter);
        }
        return filters;
    }
}
