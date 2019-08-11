package com.rabbitframework.security.web.servlet;

import org.apache.shiro.web.servlet.SimpleCookie;

/**
 * cookie
 *
 * @author: justin
 * @date: 2019-06-29 11:14
 */
public class WebCookie extends SimpleCookie {
    public WebCookie() {
        super();
    }

    public WebCookie(String name) {
        super(name);
    }
}