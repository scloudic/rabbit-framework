package com.rabbitframework.jbatis.mapping.rowmapper;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import com.tjzq.commons.utils.ReflectUtils;

public class RowMapperUtil {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static RowMapper getRowMapper(Method method, Class<?> genericClass) {
        RowMapper<?> rowMapper = null;
        Class<?> returnType = method.getReturnType();
        Class<?> returnParamType = ReflectUtils.getReturnType(method, genericClass);
        if (returnParamType.isPrimitive()) {
            returnParamType = ClassUtils.primitiveToWrapper(returnParamType);
        }
        // 判断是否返回单列 Map<String,String>
        if (isColumnType(returnParamType)) {
            if (Map.class.isAssignableFrom(returnType)) {
                rowMapper = new ColumnMapRowMapper();
            } else {
                rowMapper = new SingleColumnRowMapper(returnParamType);
            }
        } else if (returnParamType == Map.class) {
            rowMapper = new ColumnMapRowMapper();
        } else if (((returnParamType == List.class) || (returnParamType == Collection.class))) {
            rowMapper = new ListRowMapper(Object.class);
        } else if (returnParamType == Set.class) {
            rowMapper = new SetRowMapper(Object.class);
        } else {
            rowMapper = new BeanPropertyRowMapper(returnParamType);
        }
        return rowMapper;
    }


    public static boolean isColumnType(Class<?> columnTypeCandidate) {
        return String.class == columnTypeCandidate // NL
                || org.springframework.util.ClassUtils.isPrimitiveOrWrapper(columnTypeCandidate)// NL
                || Date.class.isAssignableFrom(columnTypeCandidate) // NL
                || columnTypeCandidate == byte[].class // NL
                || columnTypeCandidate == BigDecimal.class // NL
                || columnTypeCandidate == Blob.class // NL
                || columnTypeCandidate == Clob.class;
    }
}
