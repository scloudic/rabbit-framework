package com.scloudic.rabbitframework.web;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;

public class Result<T> {
    private String message;
    private T data;
    private int status;

    public Result(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(message, StatusCode.SC_OK.getValue(), data);
    }

    public static <T> Result<T> success(T data) {
        return success(ServletContextHelper.getMessage("success"), data);
    }

    public static <T> Result<T> success() {
        return success(ServletContextHelper.getMessage("success"), null);
    }

    public static <T> Result<T> failure(StatusCode statusCode, String message, T data) {
        return new Result<T>(message, statusCode.getValue(), data);
    }

    public static <T> Result<T> failure(StatusCode statusCode, String message) {
        return failure(statusCode, message, null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<T>(message, StatusCode.FAIL.getValue(), null);
    }

    public static <T> Result<T> failure() {
        return new Result<T>(ServletContextHelper.getMessage("fail"),
                StatusCode.FAIL.getValue(), null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
