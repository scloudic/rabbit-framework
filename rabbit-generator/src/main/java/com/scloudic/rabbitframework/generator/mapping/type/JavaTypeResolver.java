package com.scloudic.rabbitframework.generator.mapping.type;

import com.scloudic.rabbitframework.generator.mapping.EntityProperty;

public interface JavaTypeResolver {

    FullyQualifiedJavaType calculateJavaType(
            EntityProperty introspectedColumn);
    
    String calculateJdbcTypeName(EntityProperty introspectedColumn);
}
