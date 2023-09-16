package com.scloudic.rabbitframework.core.utils;

/**
 * 状态码
 *
 * @author: justin
 */
public enum StatusCode {
    SC_OK(200, "200成功状态"),
    FAIL(-1, "业务逻辑错误！"),
    SC_VALID_ERROR(-2, "数据验证错误"),
    SC_CACHE_ERROR(1, "缓存错误"),
    SC_BIZ_ERROR(2, "业务自定义错误"),
    SC_UN_KNOW(3, "未知错误,请与管理员联系!"),
    SC_LOGIN_ERROR(4, "登录时异常,请确认账号是否正确！"),
    CODEC(5, "逻辑编码错误,请与管理员联系"),
    SC_UNAUTHORIZED(401, "授权错误,请与管理员联系"),
    SC_PROXY_AUTHENTICATION_REQUIRED(407, "用户名或密码错误!"),
    SC_INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    TYPE_ERROR(6, "类型转换错误"),
    REFLECTION_ERROR(7, "类引用错误"),
    REDIS_LOCK_ERROR(8, "加锁失败"),
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
