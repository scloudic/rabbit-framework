package com.rabbitframework.security.web.filter.authc;

import com.tjzq.commons.utils.JsonUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆认证过虑,继承{@link FormAuthenticationFilter} 解决ajax请求过虑返回问题
 *
 * @author justin.liang
 */
public class FormAuthcFilter extends FormAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(FormAuthcFilter.class);

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        String loginUrl = getLoginUrl();
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setContentType("text/json; charset=utf-8");
        if (StringUtils.hasText(loginUrl) && (!AccessControlFilter.DEFAULT_LOGIN_URL.equals(loginUrl))) {
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
            PrintWriter printWriter = null;
            try {
                httpServletResponse.setContentType("text/json; charset=utf-8");
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("status", HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
                json.put("message", "authc.fail");
                printWriter = httpServletResponse.getWriter();
                printWriter.write(JsonUtils.toJson(json));
            } finally {
                try {
                    if (printWriter != null) {
                        printWriter.close();
                    }
                    response.flushBuffer();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }
}
