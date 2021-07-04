package com.rabbitframework.core.utils;

/**
 * 状态码
 *
 * @author: justin
 * @date: 2017-07-31 下午10:18
 */
public enum StatusCode {
    SC_OK(200, "200成功状态"),
    FAIL(-1, "逻辑错误"),
    SC_VALID_ERROR(-2, "数据验证错误"),
    SC_CACHE_ERROR(1, "缓存错误"),
    SC_BIZ_ERROR(2, "业务自定义错误"),
    SC_UN_KNOW(3, "未知错"),
    SC_LOGIN_ERROR(4, "登录时异常"),
    SC_UNAUTHORIZED(401, "授权失败"),
    SC_PROXY_AUTHENTICATION_REQUIRED(407, "用户认证失败"),
    SC_INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    ;
    private int value;
    private String msg;

    StatusCode(int value, String msg) {
        this.msg = msg;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
