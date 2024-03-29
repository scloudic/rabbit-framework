package com.scloudic.rabbitframework.web.filter.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XssFilter implements Filter {
    private List<String> excludeXssUri = new ArrayList<>();
    private boolean jsonFilter = false;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
                (HttpServletRequest) request);
        xssRequest.setExcludeXssUri(excludeXssUri);
        xssRequest.setJsonFilter(jsonFilter);
        chain.doFilter(xssRequest, response);
    }
    
    public void setJsonFilter(boolean jsonFilter) {
        this.jsonFilter = jsonFilter;
    }

    public void setExcludeXssUri(List<String> excludeXssUri) {
        this.excludeXssUri = excludeXssUri;
    }

    @Override
    public void destroy() {
    }
}