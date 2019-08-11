package com.rabbitframework.security.web.filter.authc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitframework.commons.utils.JsonUtils;

/**
 * 登陆认证过虑,继承{@link FormAuthenticationFilter} 解决ajax请求过虑返回问题
 *
 * @author justin.liang
 */
public class FormAuthcFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(FormAuthcFilter.class);

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        PrintWriter printWriter = null;
        try {
            String loginUrl = getLoginUrl();
            //ajax请求
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
            if ("XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {
                httpServletResponse.setContentType("text/json; charset=utf-8");
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("status", HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
                json.put("message", "Authentication fail");
                //httpServletResponse.setStatus(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
                printWriter = httpServletResponse.getWriter();
                printWriter.write(JsonUtils.toJsonString(json));
            } else {
                WebUtils.issueRedirect(request, response, loginUrl);
            }
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
