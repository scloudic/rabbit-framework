package com.scloudic.rabbitframework.jbatis.scripting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.scloudic.rabbitframework.jbatis.builder.BaseBuilder;
import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.mapping.ParameterMapping;
import com.scloudic.rabbitframework.jbatis.reflect.MetaClass;
import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.core.propertytoken.GenericTokenParser;
import com.scloudic.rabbitframework.core.propertytoken.TokenHandler;

public class SqlSourceBuilder extends BaseBuilder {
    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    /**
     * 解释传入的sql原脚本生成可执行的sql语句
     * <p/>
     * 并返回{@link StaticSqlSource}
     *
     * @param originalSql
     * @param parameterType
     * @param additionalParameters
     * @return
     */
    public SqlSource parse(String originalSql, Class<?> parameterType,
                           Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
    }


    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {
        private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration,
                                            Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }


        @Override
        public String handleToken(String content) {
            parameterMappings.add(builderParameterMapping(content));
            return "?";
        }

        public ParameterMapping builderParameterMapping(String content) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(content);
            Class<?> propertyType = Object.class;
            if (metaParameters.hasGetter(content)) {
                propertyType = metaParameters.getGetterType(content);
            } else {
                MetaClass metaClass = MetaClass.forClass(parameterType);
                if (metaClass.hasGetter(content)) {
                    propertyType = metaClass.getGetterType(content);
                }
            }
            builder.javaType(propertyType);
            return builder.build();
        }
    }

}
