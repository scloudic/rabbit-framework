package com.rabbitframework.example.security;

import com.rabbitframework.security.SecurityUser;
import com.rabbitframework.security.realm.SecurityAuthorizingRealm;
import com.rabbitframework.security.realm.SecurityLoginToken;
import com.rabbitframework.core.utils.PasswordUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("exampleRealm")
public class ExampleRealm extends SecurityAuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(ExampleRealm.class);

    public ExampleRealm() {
        super();
        setCacheKeyPrefix("oper_security_ream:");
        setName("operSecurityName");
    }

    /**
     * 授权认证，在配有缓存时只调用一次
     *
     * @param securityUser
     * @return
     */
    @Override
    protected AuthorizationInfo executeGetAuthorizationInfo(
            SecurityUser securityUser) {
        logger.debug("executeGetAuthorizationInfo 权限加载开始");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    /**
     * 登陆认证，登录时调用
     *
     * @param securityLoginToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo executeGetAuthenticationInfo(
            SecurityLoginToken securityLoginToken) {
        String userName = securityLoginToken.getUsername();
        String password = new String(securityLoginToken.getPassword());
        char[] pwd = password.toCharArray();
        SecurityUser securityUser = new SecurityUser();
        securityUser.setLoginName("liangjy");
        securityUser.setRealName("liangjy");
        securityUser.setUserId("liangjy");
        securityLoginToken.setPassword(pwd);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(securityUser, pwd, getName());
        return info;
    }

    public static void main(String[] args) {
        System.out.println(PasswordUtils.verify("111111", "4Zd4z63B45m6aT27Fc86ceO898bc2e40deT54a970a0oe29f"));
    }
}
