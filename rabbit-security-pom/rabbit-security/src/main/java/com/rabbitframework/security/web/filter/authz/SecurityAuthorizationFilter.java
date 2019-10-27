package com.rabbitframework.security.web.filter.authz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tjzq.commons.utils.JsonUtils;

public abstract class SecurityAuthorizationFilter extends AuthorizationFilter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityAuthorizationFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		PrintWriter printWriter = null;
		Subject subject = getSubject(request, response);
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		if ("XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {
			try {
				httpServletResponse.setContentType("text/json; charset=utf-8");
				Map<String, Object> json = new HashMap<String, Object>();
				if (subject.getPrincipal() == null) {
					json.put("status", HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
					json.put("message", "Authentication fail");
					//httpServletResponse.setStatus(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
				} else {
					json.put("status", HttpServletResponse.SC_UNAUTHORIZED);
					json.put("message", "Authorization fail");
					//httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
				printWriter = httpServletResponse.getWriter();
				printWriter.write(JsonUtils.toJsonString(json));
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
		} else {
			redirect(subject, request, response);
		}
		return false;
	}

	private void redirect(Subject subject, ServletRequest request, ServletResponse response) throws IOException {
		if (subject.getPrincipal() == null) {
			saveRequestAndRedirectToLogin(request, response);
		} else {
			// If subject is known but not authorized, redirect to the
			// unauthorized URL if there is one
			// If no unauthorized URL is specified, just return an unauthorized
			// HTTP status code
			String unauthorizedUrl = getUnauthorizedUrl();
			// SHIRO-142 - ensure that redirect _or_ error code occurs - both
			// cannot happen due to response commit:
			if (StringUtils.hasText(unauthorizedUrl)) {
				WebUtils.issueRedirect(request, response, unauthorizedUrl);
			} else {
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
	}

}
