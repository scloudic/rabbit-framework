package com.scloudic.rabbitframework.security.web.servlet;

import org.apache.shiro.web.servlet.SimpleCookie;

/**
 * cookie
 *
 * @author: justin
 */
public class SecurityWebCookie extends SimpleCookie {
    public SecurityWebCookie() {
        super();
    }

    public SecurityWebCookie(String name) {
        super(name);
    }
}