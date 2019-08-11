package com.rabbitframework.web.utils;

/**
 * 验证类
 *
 * @author: justin
 * @date: 2017-08-02 上午1:27
 */
public class FieldError {
    private String fieldName;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
