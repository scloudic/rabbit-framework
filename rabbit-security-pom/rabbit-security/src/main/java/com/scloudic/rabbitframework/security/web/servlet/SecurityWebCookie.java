package com.scloudic.rabbitframework.security.web.servlet;

import org.apache.shiro.web.servlet.SimpleCookie;

/**
 * cookie
 *
 * @author: justin
 * @date: 2019-06-29 11:14
 */
public class SecurityWebCookie extends SimpleCookie {
    public SecurityWebCookie() {
        super();
    }

    public SecurityWebCookie(String name) {
        super(name);
    }
}