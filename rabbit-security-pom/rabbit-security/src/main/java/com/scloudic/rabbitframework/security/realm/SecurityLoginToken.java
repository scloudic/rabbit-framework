package com.scloudic.rabbitframework.security.realm;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * token对象,继承 {@link UsernamePasswordToken}
 *
 * @author: justin
 */
public class SecurityLoginToken extends UsernamePasswordToken {
    public SecurityLoginToken() {
        super();
    }


    public SecurityLoginToken(final String username, final char[] password) {
        this(username, password, false, null);
    }

    public SecurityLoginToken(final String username, final String password) {
        this(username, password != null ? password.toCharArray() : null, false, null);
    }


    public SecurityLoginToken(final String username, final char[] password, final String host) {
        this(username, password, false, host);
    }

    public SecurityLoginToken(final String username, final String password, final String host) {
        this(username, password != null ? password.toCharArray() : null, false, host);
    }

    public SecurityLoginToken(final String username, final char[] password, final boolean rememberMe) {
        this(username, password, rememberMe, null);
    }

    public SecurityLoginToken(final String username, final String password, final boolean rememberMe) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, null);
    }

    public SecurityLoginToken(final String username, final char[] password,
                              final boolean rememberMe, final String host) {
        super(username, password, rememberMe, host);
    }

    public SecurityLoginToken(final String username, final String password,
                              final boolean rememberMe, final String host) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, host);
    }
}
