package com.rabbitframework.jade.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import com.rabbitframework.jade.dataaccess.KeyGenerator;
import com.rabbitframework.jade.exceptions.BindingException;
import com.rabbitframework.jade.exceptions.BuilderException;
import com.rabbitframework.jade.mapping.binding.MapperMethod;
import com.rabbitframework.jade.scripting.LanguageDriver;
import com.rabbitframework.jade.scripting.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.rabbitframework.jade.annontations.CacheNamespace;
import com.rabbitframework.jade.annontations.Create;
import com.rabbitframework.jade.annontations.Delete;
import com.rabbitframework.jade.annontations.Insert;
import com.rabbitframework.jade.annontations.Mapper;
import com.rabbitframework.jade.annontations.SQLProvider;
import com.rabbitframework.jade.annontations.Select;
import com.rabbitframework.jade.annontations.Update;
import com.rabbitframework.jade.cache.Cache;
import com.rabbitframework.jade.dataaccess.dialect.Dialect;
import com.rabbitframework.jade.mapping.BaseMapper;
import com.rabbitframework.jade.mapping.EntityProperty;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.mapping.RowBounds;
import com.rabbitframework.jade.mapping.SqlCommendType;
import com.rabbitframework.jade.mapping.binding.EntityRegistry;
import com.rabbitframework.jade.mapping.rowmapper.RowMapperUtil;
import com.tjzq.commons.propertytoken.PropertyParser;
import com.tjzq.commons.utils.ReflectUtils;
import com.tjzq.commons.utils.StringUtils;

/**
 * Mapper解析类
 */
public class MapperParser {
    private static final Logger logger = LoggerFactory.getLogger(MapperParser.class);
    private final Set<Class<? extends Annotation>> sqlAnnotationType = new HashSet<Class<? extends Annotation>>();
    private Configuration configuration;
    private Class<?> mapperInterface;
    private MapperBuilderAssistant assistant;
    private Properties properties = new Properties();
    private Class<?> genericMapper = null;

    public MapperParser(Configuration configuration, Class<?> mapperInterface) {
        this.configuration = configuration;
        this.mapperInterface = mapperInterface;
        assistant = new MapperBuilderAssistant(configuration);
        sqlAnnotationType.add(Insert.class);
        sqlAnnotationType.add(Delete.class);
        sqlAnnotationType.add(Update.class);
        sqlAnnotationType.add(Select.class);
        sqlAnnotationType.add(Create.class);
    }

    /**
     * mapper接口注解解析
     */
    public void parse() {
        try {
            Class clazz = getGenericMapper(mapperInterface);
            if (clazz != null) {
                this.genericMapper = clazz;
                EntityRegistry entityRegistry = configuration.getEntityRegistry();
                if (entityRegistry.hasEntityMap(this.genericMapper.getName())) {
                    properties.put("T", entityRegistry.getEntityMap(genericMapper.getName()).getTableName());
                }
            }
            String mapperInterfaceName = mapperInterface.getName();
            logger.trace("mapper className:" + mapperInterfaceName);
            Mapper mapperAnnotation = mapperInterface.getAnnotation(Mapper.class);
            String catalog = mapperAnnotation.catalog();
            String resource = mapperInterface.toString();
            assistant.setCatalog(catalog);
            Field[] fields = mapperInterface.getFields();
            int fieldsLength = fields.length;
            for (int i = 0; i < fieldsLength; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                String obj = field.get(field.getType()).toString();
                properties.setProperty(fieldName, obj);
            }
            if (!configuration.isMapperLoaded(resource)) {
                configuration.addLoadedMapper(resource);
                Method[] methods = mapperInterface.getMethods();
                for (Method method : methods) {
                    parseMapperStatement(method);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BuilderException(e.getMessage(), e);
        }

    }

    /**
     * mapper方法解析
     *
     * @param method
     */
    @SuppressWarnings("rawtypes")
    private void parseMapperStatement(Method method) {
        final String mappedStatementId = mapperInterface.getName() + "." + method.getName(); // 声明ID
        Class<?> parameterType = getParameterType(method);
        if (parameterType != null && "Object".equals(parameterType.getSimpleName())) {
            Type[] types = method.getGenericParameterTypes();
            if ("T".equals(types[0].getTypeName())) {
                parameterType = this.genericMapper;
            }
        }
        LanguageDriver languageDriver = configuration.getLanguageDriver();
        SQLParser sqlParser = getSQLParserByAnnotations(method, parameterType);
        if (sqlParser == null)
            return;

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
        List<KeyGenerator> keyGenerators = new ArrayList<KeyGenerator>();
        switch (sqlCommendType) {
            case INSERT:
                if (parameterType == null) {
                    break;
                }
                Class<?> paramType = sqlParser.getParamType();
                if (!configuration.getEntityRegistry().hasEntityMap(paramType.getName())) {
                    break;
                }
                List<EntityProperty> idEntityMapping = configuration.getEntityRegistry()
                        .getEntityMap(paramType.getName()).getIdProperties();
                for (EntityProperty entityProperty : idEntityMapping) {
                    KeyGenerator keyGenerator = new KeyGenerator(entityProperty.getGenerationType(),
                            entityProperty.getJavaType(), entityProperty.getProperty(), entityProperty.getColumn(),
                            entityProperty.getSelectKey());
                    keyGenerators.add(keyGenerator);
                }
                break;
            case DELETE:
            case SELECT:
                if (sqlCommendType == SqlCommendType.SELECT) {
                    rowMapper = RowMapperUtil.getRowMapper(method, genericMapper);
                }
                if (genericMapper == null) {
                    break;
                }
                if (!configuration.getEntityRegistry().hasEntityMap(genericMapper.getName())) {
                    break;
                }
                List<EntityProperty> idProperties = configuration.getEntityRegistry()
                        .getEntityMap(genericMapper.getName()).getIdProperties();
                for (EntityProperty entityProperty : idProperties) {
                    properties.put("entityId", entityProperty.getColumn());
                    break;
                }
                break;
            default:
                break;
        }
        boolean isPage = isPage(method, sqlCommendType);
        String resultSql = getSql(sqlParser, isPage, mappedStatementId, sqlCommendType);
        SqlSource sqlSource = getSqlSource(resultSql, languageDriver);
        assistant.addMappedStatement(mappedStatementId, sqlCommendType, cache, cacheKey, sqlSource, languageDriver,
                keyGenerators, rowMapper, sqlParser.isBatchUpdate());
    }

    private String getSql(SQLParser sqlParser, boolean isPage, String mappedStatementId,
                          SqlCommendType sqlCommendType) {
        String sql;
        if (isPage) {
            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, mappedStatementId,
                    sqlCommendType, this.assistant.getCatalog());
            Dialect dialect = configuration.getEnvironment().getDataSourceFactory()
                    .getDialect(statementBuilder.build());
            sql = dialect.getSQL(sqlParser.getSqlValue());
        } else {
            sql = sqlParser.getSqlValue();
        }
        if (properties.size() > 0) {
            sql = PropertyParser.parseOther("@{", "}", sql, properties);
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

    /**
     * 获取SQL解析值{@link SQLParser}
     *
     * @param method mapper方法
     * @return
     */
    private SQLParser getSQLParserByAnnotations(Method method, Class<?> paramType) {
        try {
            String sqlValue = "";
            SqlCommendType sqlCommendType = null;
            Class<? extends Annotation> sqlAnnotationType = getAnnotationType(method);
            SQLProvider sqlProviderAnnotation = method.getAnnotation(SQLProvider.class);
            if (sqlAnnotationType != null) {
                if (sqlProviderAnnotation != null) {
                    throw new BindingException(
                            "You cannot supply both a static SQL and SQLProvider to method named " + method.getName());
                }
                sqlCommendType = SqlCommendType.valueOf(sqlAnnotationType.getSimpleName().toUpperCase(Locale.ENGLISH));
                Annotation sqlAnnotation = method.getAnnotation(sqlAnnotationType);
                sqlValue = (String) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
            } else if (sqlProviderAnnotation != null) {
                sqlValue = getSQLValueBySqlProvider(sqlProviderAnnotation);
                if (StringUtils.isBlank(sqlValue)) {
                    throw new BuilderException("Error creating SQLParser for SQLProvider.method is null");
                }
                sqlCommendType = sqlProviderAnnotation.sqlType();
            }

            SQLParser sqlParser = null;
            if (sqlCommendType != null) {
                sqlParser = new SQLParser(method, sqlValue, sqlCommendType, paramType, configuration);
            }
            return sqlParser;
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

    private String getSQLValueBySqlProvider(SQLProvider sqlProviderAnnotation) throws Exception {
        String sqlValue = "";
        String providerMethod = sqlProviderAnnotation.method();
        if (StringUtils.isBlank(providerMethod)) {
            return sqlValue;
        }
        Class<?> typeClazz = sqlProviderAnnotation.type();
        for (Method method : typeClazz.getMethods()) {
            if (providerMethod.equals(method.getName())) {
                if (method.getReturnType() == String.class) {
                    sqlValue = (String) method.invoke(typeClazz.newInstance());
                    break;
                }
            }
        }
        return sqlValue;
    }

    /**
     * 获取mapper方法中参数类型
     * <p/>
     * 多个参数时使用
     * {@link MapperMethod.ParamMap}
     *
     * @param method
     * @return
     */
    private Class<?> getParameterType(Method method) {
        Class<?> parameterType = null;
        for (Class<?> mParameterType : method.getParameterTypes()) {
            if (!RowBounds.class.isAssignableFrom(mParameterType)) {
                if (parameterType == null) {
                    parameterType = mParameterType;
                } else {
                    parameterType = MapperMethod.ParamMap.class;
                    break;
                }
            }
        }
        return parameterType;
    }

    /**
     * 获取接口泛型，找出继承{@link BaseMapper}接口,获取对应的泛型。
     * 业务上mapper不允许多级父类，只存在一级多实现
     *
     * @param clazz
     * @return
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