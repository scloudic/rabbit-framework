package com.rabbitframework.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.commons.exceptions.RabbitFrameworkException;
import com.rabbitframework.security.realm.SecurityLoginToken;

/**
 * 登陆/退出公共类
 *
 * @author: justin.liang
 * @date: 16/5/7 上午1:23
 */
public class RabbitSecurityUtils {
    private static final Logger logger = LoggerFactory.getLogger(RabbitSecurityUtils.class);

    public static SecurityUser getSecurityUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = subject.getPrincipal();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (obj != null && (obj instanceof SecurityUser)) {
            return (SecurityUser) obj;
        }
        return null;
    }


    public static String getUserId() {
        SecurityUser securityUser = getSecurityUser();
        if (securityUser != null) {
            return securityUser.getUserId();
        }
        return "";
    }

    /**
     * 安全登陆,默认不记住我,返回的参数有三种状态具体可以看{@code RabbitSecurityUtils.SecurityStatus}
     *
     * @param loginName
     * @param loginPwd
     * @return
     */
    public static boolean userLogin(String loginName, String loginPwd) {
        return login(loginName, loginPwd, false);
    }

    /**
     * 安全登陆方法
     *
     * @param loginName    用户名称
     * @param loginPwd     用户密码
     * @param isRememberMe 是否记住我
     * @return
     */
    public static boolean login(String loginName, String loginPwd, boolean isRememberMe) {
        SecurityLoginToken token = new SecurityLoginToken(loginName, loginPwd);
        // 记录该令牌，如果不记录则类似购物车功能不能使用
        token.setRememberMe(isRememberMe);
        boolean isLogin = login(token);
        return isLogin;
    }

    public static boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    /**
     * 获取sessionId
     *
     * @return
     */
    public static String getSessionId() {
        return getSession().getId().toString();
    }

    public static Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getSession();
    }

    public static boolean login(SecurityLoginToken token) {
        // 如果当前已登陆,先退出登陆
        SecurityUser securityUser = getSecurityUser();
        if (securityUser != null) {
            logout();
        }
        // subject理解成权限对象。类似user
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException ex) {// 用户名没有找到
            logger.error(ex.getMessage(), ex);
            throw new LoginFailException("login.error");
        } catch (IncorrectCredentialsException ex) {// 用户名密码不匹配
            logger.error(ex.getMessage(), ex);
            throw new LoginFailException("login.error");
        } catch (AuthenticationException e) {//其他的登录错误
            logger.error(e.getMessage(), e);
            Throwable throwable = e.getCause();
            if (throwable != null && throwable instanceof RabbitFrameworkException) {
                RabbitFrameworkException frameworkException = (RabbitFrameworkException) throwable;
                throw new LoginFailException(frameworkException.getMessage());
            } else {
                throw new LoginFailException("login.error");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new LoginFailException("login.error");
        }

        if (subject.isAuthenticated()) {
            return true;
        }
        return false;
    }

    /**
     * 退出登陆
     */
    public static void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
