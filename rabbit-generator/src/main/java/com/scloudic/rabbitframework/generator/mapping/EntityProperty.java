package com.scloudic.rabbitframework.generator.mapping;

import java.sql.Types;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.generator.mapping.type.FullyQualifiedJavaType;
import com.scloudic.rabbitframework.generator.mapping.type.Jdbc4Types;
import com.scloudic.rabbitframework.generator.utils.JavaBeanUtils;

public class EntityProperty {
    private String columnName;
    private int jdbcType;
    private String jdbcTypeName;
    private boolean nullable;
    private int length;
    private int scale;
    private String javaProperty;
    private String firstUpperJavaProperty;
    private FullyQualifiedJavaType javaType;
    private String remarks;
    private boolean primaryKey = false;
    protected String defaultValue;
    private String getterMethodName;
    private String setterMethodName;
    private boolean autoincrement;

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public EntityProperty() {
        super();
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isBLOBColumn() {
        String typeName = getJdbcTypeName();
        return "BINARY".equals(typeName) || "BLOB".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "CLOB".equals(typeName) || "LONGVARBINARY".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "LONGVARCHAR".equals(typeName) || "VARBINARY".equals(typeName); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public boolean isJdbcCharacterColumn() {
        return jdbcType == Types.CHAR || jdbcType == Types.CLOB
                || jdbcType == Types.LONGVARCHAR || jdbcType == Types.VARCHAR
                || jdbcType == Jdbc4Types.LONGNVARCHAR || jdbcType == Jdbc4Types.NCHAR
                || jdbcType == Jdbc4Types.NCLOB || jdbcType == Jdbc4Types.NVARCHAR;
    }

    public String getJavaProperty() {
        return getJavaProperty(null);
    }

    public String getJavaProperty(String prefix) {
        if (prefix == null) {
            return javaProperty;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(javaProperty);

        return sb.toString();
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
        firstUpperJavaProperty = StringUtils.uppercaseFirstChar(javaProperty);
        getterMethodName = JavaBeanUtils.getGetterMethodName(javaProperty, javaType);
        setterMethodName = JavaBeanUtils.getSetterMethodName(javaProperty);
    }

    public String getGetterMethodName() {
        return getterMethodName;
    }

    public String getSetterMethodName() {
        return setterMethodName;
    }


    public String getFirstUpperJavaProperty() {
        return firstUpperJavaProperty;
    }

    public boolean isJDBCDateColumn() {
        return javaType.equals(FullyQualifiedJavaType
                .getDateInstance())
                && "DATE".equalsIgnoreCase(jdbcTypeName);
    }

    public boolean isJDBCTimeColumn() {
        return javaType.equals(FullyQualifiedJavaType
                .getDateInstance())
                && "TIME".equalsIgnoreCase(jdbcTypeName);
    }

    public String getJdbcTypeName() {
        if (jdbcTypeName == null) {
            return "OTHER";
        }
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public void setJavaType(FullyQualifiedJavaType javaType) {
        this.javaType = javaType;
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }
}
