package com.scloudic.rabbitframework.security.realm;

import com.scloudic.rabbitframework.security.SecurityUser;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空realm，主要用于分布式项目，只根据token获取用户，并不需要做登陆操作
 */
public class EmptyRealm extends SecurityAuthorizingRealm {
    private static Logger logger = LoggerFactory.getLogger(EmptyRealm.class);

    /**
     * 执行权限操作，获取权限信息,在配有缓存时只调用一次
     *
     * @param securityUser  securityUser
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo executeGetAuthorizationInfo(SecurityUser securityUser) {
        logger.warn("EmptyRealm class executeGetAuthorizationInfo()");
        return null;
    }

    /**
     * 执行登陆操作，获取登陆信息
     *
     * @param securityLoginToken securityLoginToken
     * @return AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo executeGetAuthenticationInfo(SecurityLoginToken securityLoginToken) {
        logger.warn("EmptyRealm class executeGetAuthenticationInfo()");
        return null;
    }
}
