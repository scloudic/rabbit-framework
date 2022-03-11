package com.scloudic.rabbitframework.web.utils;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.web.Result;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * hibernate验证共公类
 *
 * @author justin.liang
 */
public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> List<FieldError> validateEntity(T obj, String... fieldFilter) {
        List<FieldError> fieldErrors = new ArrayList<FieldError>();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (CollectionUtils.isEmpty(set)) {
            return fieldErrors;
        }
        fieldErrors = getFiledErrors(set, fieldFilter);
        return fieldErrors;
    }

    private static <T> List<FieldError> getFiledErrors(Set<ConstraintViolation<T>> set, String... fieldFilter) {
        List<String> filter = Arrays.asList(fieldFilter);
        List<FieldError> fieldErrors = new ArrayList<FieldError>();
        for (ConstraintViolation<T> cv : set) {
            String fieldName = cv.getPropertyPath().toString();
            if (filter.contains(fieldName)) {
                continue;
            }
            String message = cv.getMessage();
            FieldError fieldError = getFieldError(fieldName, message);
            fieldErrors.add(fieldError);
        }
        return fieldErrors;
    }

    public static FieldError getFieldError(String fieldName, String message) {
        FieldError fieldError = new FieldError();
        if (message.indexOf("{") != -1 && message.lastIndexOf("}") != -1) {
            message = message.substring(message.indexOf("{") + 1, message.lastIndexOf("}"));
        }

        fieldError.setFieldName(fieldName);
        fieldError.setErrorMessage(ServletContextHelper.getMessage(message));
        return fieldError;
    }

    public static <T> Result validateProperty(T obj, String propertyName) {
        int status = StatusCode.SC_OK.getValue();
        String msg = "";
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            status = StatusCode.SC_VALID_ERROR.getValue();
            StringBuffer errorMsg = new StringBuffer();
            for (ConstraintViolation<T> cv : set) {
                errorMsg.append("arg:");
                errorMsg.append(cv.getPropertyPath().toString());
                errorMsg.append("error:");
                errorMsg.append(cv.getMessage());
            }
            msg = errorMsg.toString();
        }
        return Result.failure(status, msg);
    }

}
