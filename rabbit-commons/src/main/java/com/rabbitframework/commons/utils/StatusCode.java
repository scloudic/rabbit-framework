package com.rabbitframework.commons.utils;

/**
 * 状态码
 *
 * @author: justin
 * @date: 2017-07-31 下午10:18
 */
public class StatusCode {
    /* 200成功状态 */
    public static final int SC_OK = 200;
    /* 授权失败,401 */
    public static final int SC_UNAUTHORIZED = 401;
    /* 404错误 */
    public static final int SC_NOT_FOUND = 404;
    /* 用户认证失败 */
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    /* 500 服务器内部错误 */
    public static final int SC_INTERNAL_SERVER_ERROR = 500;
    /* 后台逻辑错误 */
    public static final int FAIL = -1;
    /* 数据验证错误 */
    public static final int SC_VALID_ERROR = -2;
    /*缓存错误*/
    public static final int SC_CACHE_ERROR = -3;
    /*业务自定义错误*/
    public static final int SC_BIZ_ERROR = -4;
    /*重定向*/
    public static final int SC_TEMPORARY_REDIRECT = 307;

    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    public static final int SC_BAD_REQUEST = 400;

    /**
     * Status code (405) indicating that the method specified in the
     * <code><em>Request-Line</em></code> is not allowed for the resource
     * identified by the <code><em>Request-URI</em></code>.
     */
    public static final int SC_METHOD_NOT_ALLOWED = 405;
}
