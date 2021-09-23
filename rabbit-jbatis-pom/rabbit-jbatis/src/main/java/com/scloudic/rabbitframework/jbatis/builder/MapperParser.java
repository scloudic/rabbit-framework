package com.scloudic.rabbitframework.jbatis.builder;

import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.core.utils.ReflectUtils;
import com.scloudic.rabbitframework.jbatis.annontations.*;
import com.scloudic.rabbitframework.jbatis.cache.Cache;
import com.scloudic.rabbitframework.jbatis.dataaccess.KeyGenerator;
import com.scloudic.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.*;
import com.scloudic.rabbitframework.jbatis.mapping.binding.MapperMethod;
import com.scloudic.rabbitframework.jbatis.mapping.rowmapper.RowMapperUtil;
import com.scloudic.rabbitframework.jbatis.scripting.LanguageDriver;
import com.scloudic.rabbitframework.jbatis.scripting.SqlSource;
import com.scloudic.rabbitframework.jbatis.scripting.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Mapper解析类
 */
public class MapperParser {
    private static final Logger logger = LoggerFactory.getLogger(MapperParser.class);
    private final Set<Class<? extends Annotation>> sqlAnnotationType = new HashSet<Class<? extends Annotation>>();
    private Configuration configuration;
    private Class<?> mapperInterface;
    private MapperBuilderAssistant assistant;
    private Class<?> genericMapper = null;

    public MapperParser(Configuration configuration, Class<?> mapperInterface) {
        this.configuration = configuration;
        this.mapperInterface = mapperInterface;
        this.genericMapper = getGenericMapper(mapperInterface);
        assistant = new MapperBuilderAssistant(configuration);
        sqlAnnotationType.add(Insert.class);
        sqlAnnotationType.add(Delete.class);
        sqlAnnotationType.add(Update.class);
        sqlAnnotationType.add(Select.class);
        sqlAnnotationType.add(Create.class);
    }

    public void parse() {
        String resource = mapperInterface.getName();
        try {
            if (configuration.isMapperLoaded(resource)) {
                return;
            }
            configuration.addLoadedMapper(resource);
            Mapper mapperAnnotation = mapperInterface.getAnnotation(Mapper.class);
            assistant.setCatalog(mapperAnnotation.catalog());
            Method[] methods = mapperInterface.getMethods();
            for (Method method : methods) {
                parseMapperStatement(method);
            }
        } catch (Exception e) {
            logger.error(resource + ",error:" + e.getMessage(), e);
            throw new BuilderException(resource + ",error:" + e.getMessage(), e);
        }

    }

    @SuppressWarnings("rawtypes")
    private void parseMapperStatement(Method method) {
        String methodName = method.getName();
        final String mappedStatementId = mapperInterface.getName() + "." + methodName; // 声明ID
        Class<?> parameterType = getParameterType(method);
        LanguageDriver languageDriver = configuration.getLanguageDriver();
        BaseSQLParser sqlParser = getBaseSQLParser(method, parameterType);
        if (sqlParser == null) {
            logger.error(mappedStatementId + "is null");
            return;
        }

        SqlCommendType sqlCommendType = sqlParser.getSqlCommendType();
        CacheNamespace cacheNamespace = method.getAnnotation(CacheNamespace.class);
        Cache cache = null;
        String[] cacheKey = null;
        if (cacheNamespace != null) {
            String pool = cacheNamespace.pool();
            cache = configuration.getCache(pool);
            cacheKey = cacheNamespace.key();
        }
        RowMapper rowMapper = null;
        if (sqlCommendType == SqlCommendType.SELECT) {
            rowMapper = RowMapperUtil.getRowMapper(method, genericMapper);
        }
        List<KeyGenerator> keyGenerators = sqlParser.getKeyGenerators();

        boolean isPage = isPage(method, sqlCommendType);
        String resultSql = getSql(sqlParser, isPage, mappedStatementId, sqlCommendType);
        SqlSource sqlSource = getSqlSource(resultSql, languageDriver);
        assistant.addMappedStatement(mappedStatementId, sqlCommendType, cache, cacheKey, sqlSource, languageDriver,
                keyGenerators, rowMapper, sqlParser.isBatchUpdate());
    }

    private String getSql(BaseSQLParser sqlParser, boolean isPage, String mappedStatementId,
                          SqlCommendType sqlCommendType) {
        String sql;
        if (isPage) {
            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, mappedStatementId,
                    sqlCommendType, this.assistant.getCatalog());
            Dialect dialect = configuration.getEnvironment().getDataSourceFactory()
                    .getDialect(statementBuilder.build());
            sql = dialect.getSQL(sqlParser.parserSQL());
        } else {
            sql = sqlParser.parserSQL();
        }
        return sql;
    }

    private boolean isPage(Method method, SqlCommendType commendType) {
        boolean pageFlag = false;
        if (commendType != SqlCommendType.SELECT) {
            return pageFlag;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (RowBounds.class.isAssignableFrom(parameterTypes[i])) {
                pageFlag = true;
                break;
            }
        }
        return pageFlag;
    }

    private SqlSource getSqlSource(String sqlValue, LanguageDriver languageDriver) {
        String sqlBuilder = "<script>" +
                sqlValue +
                "</script>";
        return languageDriver.createSqlSource(configuration, sqlBuilder);
    }

    private BaseSQLParser getBaseSQLParser(Method method, Class<?> paramType) {
        try {
            String methodName = method.getName();
            BaseDefaultMethod baseDefaultMethod = configuration.getBaseDefaultMethodMap(methodName);
            if (baseDefaultMethod != null && genericMapper != null) {
                BaseSQLParser baseSQLParser = ClassUtils.newInstance(baseDefaultMethod.getClazz());
                baseSQLParser.setSqlScript(baseDefaultMethod.getSql());
                baseSQLParser.setConfiguration(configuration);
                baseSQLParser.setGenericClass(genericMapper);
                baseSQLParser.setParamType(paramType);
                return baseSQLParser;
            }
            Class<? extends Annotation> sqlAnnotationType = getAnnotationType(method);
            if (sqlAnnotationType == null) {
                return null;
            }
            SqlCommendType sqlCommendType = SqlCommendType.valueOf(sqlAnnotationType.getSimpleName().toUpperCase(Locale.ENGLISH));
            Annotation sqlAnnotation = method.getAnnotation(sqlAnnotationType);
            String sqlValue = (String) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
            BaseSQLParser baseSQLParser = null;
            switch (sqlCommendType) {
                case SELECT:
                    baseSQLParser = new CustomerSelect();
                    break;
                case UPDATE:
                    baseSQLParser = new CustomerUpdate();
                    break;
                case INSERT:
                    Insert insert = method.getAnnotation(Insert.class);
                    if (insert.batch()) {
                        baseSQLParser = new CustomerBatchInsert();
                    } else {
                        baseSQLParser = new CustomerInsert();
                    }
                    break;
                case DELETE:
                    baseSQLParser = new CustomerDel();
                    break;
                case CREATE:
                    baseSQLParser = new CustomerCreate();
                    break;
            }
            if (baseSQLParser != null) {
                baseSQLParser.setSqlScript(sqlValue);
                baseSQLParser.setConfiguration(configuration);
                baseSQLParser.setGenericClass(null);
                baseSQLParser.setParamType(paramType);
            }
            return baseSQLParser;
        } catch (Exception e) {
            throw new BuilderException("Could not find value method on SQL annontation. Cause: " + e, e);
        }
    }

    private Class<? extends Annotation> getAnnotationType(Method method) {
        for (Class<? extends Annotation> type : sqlAnnotationType) {
            Annotation annotation = method.getAnnotation(type);
            if (annotation != null) {
                return type;
            }
        }
        return null;
    }

    private Class<?> getParameterType(Method method) {
        Class<?> parameterType = null;
        for (Class<?> mParameterType : method.getParameterTypes()) {
            if (RowBounds.class.isAssignableFrom(mParameterType)) {
                continue;
            }
            if (parameterType == null) {
                parameterType = mParameterType;
            } else {
                parameterType = MapperMethod.ParamMap.class;
                break;
            }
        }
        return parameterType;
    }

    /**
     * 获取接口泛型，找出继承{@link BaseMapper}接口,获取对应的泛型。
     * 业务上mapper不允许多级父类，只存在一级多实现
     *
     * @param clazz clazz
     * @return Class
     */
    private Class<?> getGenericMapper(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        int genericInterfacesLength = genericInterfaces.length;
        Class rawType = null;
        if (genericInterfacesLength > 0) {
            for (Type type : genericInterfaces) {
                /*如果为真,表示mapper子类没有泛型类*/
                if (type == BaseMapper.class) {
                    break;
                }
                if (type instanceof Class) {
                    continue;
                }
                ParameterizedType parameterizedType = ((ParameterizedType) type);
                Type params = parameterizedType.getRawType();
                if (params == BaseMapper.class) {
                    Type actualTypeArgs = parameterizedType.getActualTypeArguments()[0];
                    rawType = ReflectUtils.getGenericClassByType(actualTypeArgs);
                }
            }
        }
        return rawType;
    }
}