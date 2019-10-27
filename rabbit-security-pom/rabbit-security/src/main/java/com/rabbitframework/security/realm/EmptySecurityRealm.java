package com.rabbitframework.security.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认realm实现
 *
 * @author: justin
 * @date: 2017-07-20 下午3:45
 */
public class EmptySecurityRealm extends SecurityAuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(EmptySecurityRealm.class);

    public EmptySecurityRealm() {
        super();
        setName("empty_realm");
    }

    /**
     * 授权认证，在配有缓存时只调用一次
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.debug("AuthorizationInfo:" + getName());
        return null;
    }

    /**
     * 登陆认证，登录时调用
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.debug("AuthenticationInfo:" + getName());
        return null;
    }
}
