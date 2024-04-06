package com.scloudic.rabbitframework.web.exceptions;

import com.scloudic.rabbitframework.core.exceptions.RabbitFrameworkException;
import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.web.Result;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import com.scloudic.rabbitframework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 *
 * @author justin.liang
 */
@ControllerAdvice
public class ExceptionMapperSupport {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapperSupport.class);
    //是否前后端分离
    private boolean frontBlack = true;
    //登录界面跳转地址 401
    private String loginUrl = "";
    //权限跳转地址 407
    private String unauthorizedUrl = "";
    //系统异常,500错误
    private String sys500ErrorUrl = "";
    //404错误跳转地址
    private String sys404ErrorUrl = "";
    //405错误跳转地址
    private String sys405ErrorUrl = "";

    private String otherError = "";

    @ExceptionHandler(RabbitFrameworkException.class)
    @ResponseBody
    public Result rabbitFrameworkException(RabbitFrameworkException e,
                                           HandlerMethod handlerMethod,
                                           HttpServletResponse response,
                                           HttpServletRequest request) {
        logger.error("异常信息：" + e.getMessage(), e);
        logger.error("异常类：{}", handlerMethod.getBean().getClass());
        logger.error("异常请求地址：{}", request.getRequestURL());
        String msg = e.getDescription();
        if (StringUtils.isBlank(msg)) {
            msg = e.getMessage();
        }
        String message = ServletContextHelper.getMessage(msg);
        return getResponseByStatus(e.getStatus(), message, response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e, HandlerMethod handlerMethod,
                                   HttpServletRequest request) {
        logger.error("异常信息：" + e.getMessage(), e);
        logger.error("异常类：{}", handlerMethod.getBean().getClass()
                , handlerMethod.getMethod().getName());
        logger.error("异常请求地址：{}", request.getRequestURL());
        String message = ServletContextHelper.getMessage("exception.error");
        return Result.failure(StatusCode.SC_INTERNAL_SERVER_ERROR.getValue(), message);
    }

    public Result getResponseByStatus(StatusCode statusCode, String message, HttpServletResponse response) {
        Result result = Result.failure(statusCode.getValue(), message);
        try {
            switch (statusCode) {
                case SC_INTERNAL_SERVER_ERROR:
                case SC_UNAUTHORIZED:
                case SC_PROXY_AUTHENTICATION_REQUIRED:
                    result = getResponseByStatus(statusCode.getValue(), message, response);
                    break;
                case SC_BIZ_ERROR:
                case SC_UN_KNOW:
                    if (!isAsync()) {
                        response.sendRedirect(dislodgeFirstSlash(otherError));
                    }
                    break;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return result;
    }

    public Result getResponseByStatus(int httpStatus, String message, HttpServletResponse response) {
        try {
            switch (httpStatus) {
                case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                    if (!isAsync()) {
                        response.sendRedirect(dislodgeFirstSlash(getSys500ErrorUrl()));
                    }
                    break;
                case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                    if (!isAsync()) {
                        response.sendRedirect(
                                dislodgeFirstSlash(getSys405ErrorUrl()));
                    }
                    break;
                case HttpServletResponse.SC_UNAUTHORIZED:
                    if (!isAsync()) {
                        response.sendRedirect(
                                dislodgeFirstSlash(getUnauthorizedUrl()));
                    }
                    break;
                case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED:
                    if (!isAsync()) {
                        response.sendRedirect(
                                dislodgeFirstSlash(getLoginUrl()));
                    }
                    break;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return Result.failure(httpStatus, message);
    }

    private boolean isAsync() {
        if (isFrontBlack()) {
            return true;
        }
        String requestedWith = WebUtils.getRequest().getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }

    public boolean isFrontBlack() {
        return frontBlack;
    }

    public void setFrontBlack(boolean frontBlack) {
        this.frontBlack = frontBlack;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getSys500ErrorUrl() {
        return sys500ErrorUrl;
    }

    public void setSys500ErrorUrl(String sys500ErrorUrl) {
        this.sys500ErrorUrl = sys500ErrorUrl;
    }

    public String getSys404ErrorUrl() {
        return sys404ErrorUrl;
    }

    public void setSys404ErrorUrl(String sys404ErrorUrl) {
        this.sys404ErrorUrl = sys404ErrorUrl;
    }

    public String getSys405ErrorUrl() {
        return sys405ErrorUrl;
    }

    public void setSys405ErrorUrl(String sys405ErrorUrl) {
        this.sys405ErrorUrl = sys405ErrorUrl;
    }

    public String getOtherError() {
        return otherError;
    }

    public void setOtherError(String otherError) {
        this.otherError = otherError;
    }

    private static String dislodgeFirstSlash(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        if (url.charAt(0) == '/') {
            return url.substring(1);
        }
        return url;
    }
}
