package com.scloudic.rabbitframework.security.web.servlet;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityHttpServletRequest extends ShiroHttpServletRequest {
    private HttpServletRequest wrapped;


    public SecurityHttpServletRequest(HttpServletRequest wrapped, ServletContext servletContext, boolean httpSessions) {
        super(wrapped, servletContext, httpSessions);
        this.wrapped = wrapped;
    }

    public HttpSession getHttpSession() {
        return wrapped.getSession();
    }

    public HttpServletRequest getOrgRequest() {
        return wrapped;
    }
}
