package com.scloudic.rabbitframework.jbatis.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.scloudic.rabbitframework.jbatis.annontations.Mapper;
import com.scloudic.rabbitframework.jbatis.annontations.Table;
import com.scloudic.rabbitframework.jbatis.dataaccess.Environment;
import com.scloudic.rabbitframework.jbatis.dataaccess.JdbcTemplateHolder;
import com.scloudic.rabbitframework.jbatis.dataaccess.SqlDataAccess;
import com.scloudic.rabbitframework.jbatis.intercept.Interceptor;
import com.scloudic.rabbitframework.jbatis.intercept.InterceptorChain;
import com.scloudic.rabbitframework.jbatis.mapping.BaseDefaultMethod;
import com.scloudic.rabbitframework.jbatis.mapping.BoundSql;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.jbatis.scripting.LanguageDriver;
import com.scloudic.rabbitframework.jbatis.scripting.LanguageDriverImpl;
import com.scloudic.rabbitframework.jbatis.cache.Cache;
import com.scloudic.rabbitframework.jbatis.scripting.sql.BaseSQLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.jbatis.executor.CacheExecutor;
import com.scloudic.rabbitframework.jbatis.executor.Executor;
import com.scloudic.rabbitframework.jbatis.executor.ParameterHandler;
import com.scloudic.rabbitframework.jbatis.executor.PreparedStatementHandler;
import com.scloudic.rabbitframework.jbatis.executor.SimpleExecutor;
import com.scloudic.rabbitframework.jbatis.executor.StatementHandler;
import com.scloudic.rabbitframework.jbatis.mapping.binding.EntityRegistry;
import com.scloudic.rabbitframework.jbatis.mapping.binding.MapperRegistry;
import com.scloudic.rabbitframework.core.reflect.factory.DefaultObjectFactory;
import com.scloudic.rabbitframework.core.reflect.factory.ObjectFactory;
import com.scloudic.rabbitframework.core.utils.ClassUtils;

/**
 * Dbase初始化类,启动时加载
 */
public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    protected final Map<String, MappedStatement> mappedStatements = new StrictMap<MappedStatement>(
            "Mapped Statements collection");
    protected final EntityRegistry entityRegistry = new EntityRegistry(this);
    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);
    protected final Map<String, Cache> caches = new HashMap<String, Cache>();
    private InterceptorChain interceptorChain = new InterceptorChain();
    private Environment environment = null;
    private Properties variables = new Properties();
    private LanguageDriver languageDriver = new LanguageDriverImpl();
    protected final Set<String> loadedMappers = new HashSet<String>();
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected Map<String, BaseDefaultMethod> baseDefaultMethodMap = new HashMap<>();

    public Configuration() {
        BaseDefaultMethod[] baseDefaultMethods = BaseDefaultMethod.values();
        for (BaseDefaultMethod baseDefaultMethod : baseDefaultMethods) {
            baseDefaultMethodMap.put(baseDefaultMethod.getMethod(), baseDefaultMethod);
        }
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public Properties getVariables() {
        return variables;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptorChain.addInterceptor(interceptor);
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

    public void addCaches(Map<String, Cache> caches) {
        if (caches != null) {
            this.caches.putAll(caches);
        }
    }

    public void addCache(String key, Cache cache) {
        caches.put(key, cache);
    }

    public Cache getCache(String key) {
        return caches.get(key);
    }

    public boolean hasCache() {
        return caches.size() > 0;
    }

    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory);
    }

    public void addEntitys(String... packageName) {
        List<Class<?>> clazzs = ClassUtils.getClassNamePackage(packageName);
        for (Class<?> clazz : clazzs) {
            addEntity(clazz);
        }
    }

    public void addEntity(Class<?> entity) {
        Table entityAnnotation = entity.getAnnotation(Table.class);
        if (null == entityAnnotation) {
            logger.warn(entity.getName() + " haven't @Table annotation");
            return;
        }
        this.getEntityRegistry().addEntity(entity);
    }

    public void addMappers(String[] packageNames) {
        List<Class<?>> clazzes = ClassUtils.getClassNamePackage(packageNames);
        for (Class<?> clazz : clazzes) {
            addMapper(clazz);
        }
    }

    public <T> void addMapper(Class<T> mapperInterface) {
        Mapper mapperAnnotation = mapperInterface.getAnnotation(Mapper.class);
        if (null == mapperAnnotation) {
            logger.warn(mapperInterface + " haven't @Mapper annotation");
            return;
        }
        getMapperRegistry().addMapper(mapperInterface);
    }

    public <T> T getMapper(Class<T> mapperInterface, SqlDataAccess sqlDataAccess) {
        return getMapperRegistry().getMapper(mapperInterface, sqlDataAccess);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public Collection<String> getMappedStatementNames() {
        return mappedStatements.keySet();
    }

    public Collection<MappedStatement> getMappedStatements() {
        return mappedStatements.values();
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public boolean hasStatement(String statementName) {
        return mappedStatements.containsKey(statementName);
    }

    public Executor newExecutor(Cache cache) {
        JdbcTemplateHolder jdbcTemplateHolder = new JdbcTemplateHolder(
                environment);
        Executor executor = new SimpleExecutor(this, jdbcTemplateHolder);
        if (cache != null) {
            executor = new CacheExecutor(executor);
        }
        return executor;
    }

    public StatementHandler newStatementHandler(
            MappedStatement mappedStatement, Object[] parameterObject,
            BoundSql... boundSql) {
        StatementHandler statementHandler = new PreparedStatementHandler(
                mappedStatement, parameterObject, boundSql);
        statementHandler = (StatementHandler) interceptorChain
                .pluginAll(statementHandler);
        return statementHandler;
    }

    public ParameterHandler newParameterHandler(
            MappedStatement mappedStatement, Object parameterObject,
            BoundSql boundSql) {
        ParameterHandler parameterHandler = mappedStatement.getLanguageDriver()
                .createParameterHandler(mappedStatement, parameterObject,
                        boundSql);
        return parameterHandler;
    }


    public boolean isMapperLoaded(String mapperResource) {
        return loadedMappers.contains(mapperResource);
    }


    public void addLoadedMapper(String mapperResource) {
        loadedMappers.add(mapperResource);
    }

    public LanguageDriver getLanguageDriver() {
        return languageDriver;
    }

    public BaseDefaultMethod getBaseDefaultMethodMap(String key) {
        return baseDefaultMethodMap.get(key);
    }

    protected static class StrictMap<V> extends HashMap<String, V> {
        private static final long serialVersionUID = 3367968086078794771L;
        private String name;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }

        public StrictMap(String name) {
            super();
            this.name = name;
        }

        public StrictMap(String name, Map<String, ? extends V> m) {
            super(m);
            this.name = name;
        }

        @SuppressWarnings("unchecked")
        public V put(String key, V value) {
            if (containsKey(key))
                throw new IllegalArgumentException(name
                        + " already contains value for " + key);
            if (key.contains(".")) {
                final String shortKey = getShortName(key);
                if (super.get(shortKey) == null) {
                    super.put(shortKey, value);
                } else {
                    super.put(shortKey, (V) new Ambiguity(shortKey));
                }
            }
            return super.put(key, value);
        }

        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                throw new IllegalArgumentException(name
                        + " does not contain value for " + key);
            }
            if (value instanceof Ambiguity) {
                throw new IllegalArgumentException(
                        ((Ambiguity) value).getSubject()
                                + " is ambiguous in "
                                + name
                                + " (try using the full name including the namespace, or rename one of the entries)");
            }
            return value;
        }


        private String getShortName(String key) {
            final String[] keyparts = key.split("\\.");
            final String shortKey = keyparts[keyparts.length - 1];
            return shortKey;
        }

        protected static class Ambiguity {
            private String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return subject;
            }
        }
    }
}
