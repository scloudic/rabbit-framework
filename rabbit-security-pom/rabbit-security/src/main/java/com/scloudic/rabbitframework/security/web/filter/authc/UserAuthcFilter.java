package com.scloudic.rabbitframework.security.web.filter.authc;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.security.web.filter.RabbitSecurityFilter;
import com.scloudic.rabbitframework.security.web.filter.RedirectUtils;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 用户登录请求验证过滤器
 * <p>
 * 实现{@link UserFilter#redirectToLogin(ServletRequest, ServletResponse)}方法，处理未登录时跳转
 *
 * @author justin.liang
 * @since 3.3.1
 */
public class UserAuthcFilter extends UserFilter implements RabbitSecurityFilter {
    private boolean frontEndSeparate = true;

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        String loginUrl = getLoginUrl();
        RedirectUtils.redirect(request, response, loginUrl, StatusCode.SC_PROXY_AUTHENTICATION_REQUIRED, frontEndSeparate);
    }

    @Override
    public void setFrontEndSeparate(boolean frontEndSeparate) {
        this.frontEndSeparate = frontEndSeparate;
    }
}
