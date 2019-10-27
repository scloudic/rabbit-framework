package com.rabbitframework.security.web.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSecurityFilter extends AbstractShiroFilter {
	private static final Logger log = LoggerFactory.getLogger(AbstractShiroFilter.class);
	private String filterUrl = "";
	private PatternMatcher pathMatcher = new AntPathMatcher();

	protected AbstractSecurityFilter() {
		super();
	}

	@Override
	protected ServletResponse wrapServletResponse(HttpServletResponse orig, ShiroHttpServletRequest request) {
		return new SecurityHttpServletResponse(orig, getServletContext(), request);
	}

	@Override
	protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
			final FilterChain chain) throws ServletException, IOException {
//		final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
//		final ServletResponse response = prepareServletResponse(request, servletResponse, chain);
		boolean filter = true;
		if (StringUtils.hasLength(filterUrl)) {
			String[] filterUrls = filterUrl.split(",");
			int length = filterUrls.length;
			for (int i = 0; i < length; i++) {
				String url = filterUrls[i];
				if (pathsMatch(url, servletRequest)) {
					filter = false;
					//executeChain(request, response, chain);
					 chain.doFilter(servletRequest, servletResponse);
					break;
				}
			}
		}
		if (filter) {
			super.doFilterInternal(servletRequest, servletResponse, chain);
			// final Subject subject = createSubject(request, response);
			// // noinspection unchecked
			// subject.execute(new Callable() {
			// public Object call() throws Exception {
			// updateSessionLastAccessTime(request, response);
			// executeChain(request, response, chain);
			// return null;
			// }
			// });
		}
	}

	protected boolean pathsMatch(String path, ServletRequest request) {
		String requestURI = getPathWithinApplication(request);
		log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
		return pathsMatch(path, requestURI);
	}

	protected boolean pathsMatch(String pattern, String path) {
		return pathMatcher.matches(pattern, path);
	}

	protected String getPathWithinApplication(ServletRequest request) {
		return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
	}

	public String getFilterUrl() {
		return filterUrl;
	}

	public void setFilterUrl(String filterUrl) {
		this.filterUrl = filterUrl;
	}

}
