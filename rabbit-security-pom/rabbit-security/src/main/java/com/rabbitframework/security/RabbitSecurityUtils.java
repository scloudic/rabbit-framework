package com.rabbitframework.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	/**
	 * 安全登陆,默认不记住我
	 *
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	@Deprecated
	public static void login(String loginName, String loginPwd) {
		login(loginName, loginPwd, false);
	}

	/**
	 * 安全登陆,默认不记住我,返回的参数有三种状态具体可以看{@code RabbitSecurityUtils.SecurityStatus}
	 * 
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public static void userLogin(String loginName, String loginPwd) {
		login(loginName, loginPwd, false);

	}

	/**
	 * 安全登陆方法
	 *
	 * @param loginName
	 *            用户名称
	 * @param loginPwd
	 *            用户密码
	 * @param isRemeberMe
	 *            是否记住我
	 * @return
	 */
	public static void login(String loginName, String loginPwd, boolean isRemeberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(loginName, loginPwd);
		// 记录该令牌，如果不记录则类似购物车功能不能使用
		token.setRememberMe(isRemeberMe);
		// subject理解成权限对象。类似user
		Subject subject = SecurityUtils.getSubject();
		// 如果当前已登陆,先退出登陆
		SecurityUser securityUser = getSecurityUser();
		if (securityUser != null) {
			logout();
		}
		subject.login(token);
	}

	public static boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated();
	}

	public static boolean login(UsernamePasswordToken token) {
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
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
