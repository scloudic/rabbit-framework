package com.rabbitframework.security.web.servlet;

import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.util.RegExPatternMatcher;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractSecurityFilter extends AbstractShiroFilter {
    private static final Logger logger = LoggerFactory.getLogger(AbstractShiroFilter.class);
    private String filterUrl = "";
    private PatternMatcher pathMatcher = new AntPathMatcher();
    PatternMatcher regPathMatcher = new RegExPatternMatcher();

    protected AbstractSecurityFilter() {
        super();
    }


    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
                                    final FilterChain chain) throws ServletException, IOException {
        if (StringUtils.hasLength(filterUrl)) {
            String requestURI = getPathWithinApplication(servletRequest);
            if (regPathMatcher.matches(filterUrl, requestURI)) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        super.doFilterInternal(servletRequest, servletResponse, chain);
    }

    @Override
    protected ServletResponse wrapServletResponse(HttpServletResponse orig, ShiroHttpServletRequest request) {
        return new SecurityHttpServletResponse(orig, getServletContext(), request);
    }

    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        logger.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathMatcher.matches(path, requestURI);
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }

    public void setFilterUrl(String filterUrl) {
        this.filterUrl = filterUrl;
    }

}
