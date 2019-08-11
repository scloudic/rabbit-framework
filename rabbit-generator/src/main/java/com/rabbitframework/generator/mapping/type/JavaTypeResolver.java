package com.rabbitframework.generator.mapping.type;

import com.rabbitframework.generator.mapping.EntityProperty;

public interface JavaTypeResolver {

    FullyQualifiedJavaType calculateJavaType(
            EntityProperty introspectedColumn);
    
    String calculateJdbcTypeName(EntityProperty introspectedColumn);
}
