package com.rabbitframework.security.web.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter;
import org.apache.shiro.web.filter.authz.PortFilter;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.filter.session.NoSessionCreationFilter;

import com.rabbitframework.security.web.filter.authc.FormAuthcFilter;
import com.rabbitframework.security.web.filter.authz.PermissionsAuthorizationFilter;
import com.rabbitframework.security.web.filter.authz.RolesAuthorizationFilter;
import com.rabbitframework.security.web.filter.authz.RolesOrAuthorizationFilter;
import com.rabbitframework.security.web.filter.authz.UriPermissionsFilter;

public enum SecurityFilter {
	anon(AnonymousFilter.class),
	authc(FormAuthcFilter.class), 
	authcBasic(BasicHttpAuthenticationFilter.class),
	logout(LogoutFilter.class), 
	noSessionCreation(NoSessionCreationFilter.class), 
	perms(PermissionsAuthorizationFilter.class), 
	port(PortFilter.class),
	rest(HttpMethodPermissionFilter.class),
	roles(RolesAuthorizationFilter.class),
	rolesOr(RolesOrAuthorizationFilter.class),
	ssl(SslFilter.class),
	user(UserFilter.class),
	uriPerms(UriPermissionsFilter.class);
	
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
