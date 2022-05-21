package com.scloudic.rabbitframework.web.utils;

/**
 * 验证类
 *
 * @author: justin
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
