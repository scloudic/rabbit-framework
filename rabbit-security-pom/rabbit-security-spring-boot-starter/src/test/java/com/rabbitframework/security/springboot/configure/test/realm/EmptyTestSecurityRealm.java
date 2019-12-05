package com.rabbitframework.security.springboot.configure.test.realm;

import com.rabbitframework.security.SecurityUser;
import com.rabbitframework.security.realm.SecurityAuthorizingRealm;
import com.rabbitframework.security.realm.SecurityLoginToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 默认realm实现
 *
 * @author: justin
 * @date: 2017-07-20 下午3:45
 */
@Component("emptyTestSecurityRealm")
public class EmptyTestSecurityRealm extends SecurityAuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(EmptyTestSecurityRealm.class);

    public EmptyTestSecurityRealm() {
        super();
        setName("empty_realm");
    }

    /**
     * 获取权限信息,在配有缓存时只调用一次
     *
     * @param securityUser
     * @return
     */
    @Override
    protected AuthorizationInfo executeGetAuthorizationInfo(SecurityUser securityUser) {
        return null;
    }

    /**
     * 执行登陆操作，获取登陆信息
     *
     * @param securityLoginToken
     * @return
     */
    @Override
    protected AuthenticationInfo executeGetAuthenticationInfo(SecurityLoginToken securityLoginToken) {
        return null;
    }
}
