package com.rabbitframework.security.web.filter;

import com.rabbitframework.core.utils.CommonResponseUrl;
import com.rabbitframework.core.utils.StatusCode;
import com.rabbitframework.core.utils.JsonUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 重定向公共类
 *
 * @since 3.3.1
 */
public class RedirectUtils {
    private static Logger logger = LoggerFactory.getLogger(RedirectUtils.class);

    public static void redirect(ServletRequest request, ServletResponse response,
                                String loginUrl, StatusCode statusCode) throws IOException {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setContentType("text/json; charset=utf-8");
        if (!isAsync(request)) {
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
            PrintWriter printWriter = null;
            try {
                httpServletResponse.setContentType("text/json; charset=utf-8");
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("status", statusCode.getValue());
                json.put("message", statusCode.getValue() + ".error");
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


    private static boolean isAsync(ServletRequest request) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if (CommonResponseUrl.isFrontBlack()) {
            return true;
        }
        String requestedWith = httpServletRequest.getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }
}
