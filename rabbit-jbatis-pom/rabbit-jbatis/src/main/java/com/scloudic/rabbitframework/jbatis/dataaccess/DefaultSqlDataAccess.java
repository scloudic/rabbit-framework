package com.scloudic.rabbitframework.jbatis.dataaccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scloudic.rabbitframework.jbatis.exceptions.BindingException;
import com.scloudic.rabbitframework.jbatis.exceptions.PersistenceException;
import com.scloudic.rabbitframework.jbatis.exceptions.TooManyResultsException;
import org.springframework.dao.support.DataAccessUtils;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.executor.Executor;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.core.reflect.factory.ObjectFactory;

public class DefaultSqlDataAccess implements SqlDataAccess {
    private Configuration configuration;

    public DefaultSqlDataAccess(Configuration configuration) {
        this.configuration = configuration;

    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T selectOne(String statement, String dynamicSQL) {
        return selectOne(statement, null, dynamicSQL);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter, String dynamicSQL) {
        List<T> list = selectList(statement, parameter, dynamicSQL);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException(
                    "Expected one results (or null) to be returned by selectOne(), but found: "
                            + list.size());
        }
        return null;
    }

    @Override
    public <E> List<E> selectList(String statement, String dynamicSQL) {
        return selectList(statement, null, dynamicSQL);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, String dynamicSQL) {
        return selectList(statement, parameter, null, dynamicSQL);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter,
                                  RowBounds rowBounds, String dynamicSQL) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            Object obj = wrapCollection(parameter);
            Executor executor = configuration.newExecutor(ms.getCache());
            return executor.query(ms, obj, rowBounds, dynamicSQL);
        } catch (Exception e) {
            throw new PersistenceException("Error querying database.  Cause: "
                    + e, e);
        }

    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey, String dynamicSQL) {
        return this.selectMap(statement, null, mapKey, null, dynamicSQL);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter,
                                      String mapKey, String dynamicSQL) {
        return this.selectMap(statement, parameter, mapKey, null, dynamicSQL);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> selectMap(String statement, Object parameter,
                                      String mapKey, RowBounds rowBounds, String dynamicSQL) {
        final List<?> list = selectList(statement, parameter, rowBounds, dynamicSQL);
        if (list == null || list.size() == 0) {
            return new HashMap<K, V>();
        }
        if (mapKey == null) {
            return (Map<K, V>) DataAccessUtils.requiredSingleResult(list);
        }
        ObjectFactory objectFactory = configuration.getObjectFactory();
        Map<K, V> selectedMap = objectFactory.create(Map.class);
        for (Object o : list) {
            V value = (V) o;
            MetaObject mo = MetaObject.forObject(o, objectFactory);
            final K key = (K) mo.getValue(mapKey);
            selectedMap.put(key, value);
        }
        return selectedMap;
    }

    @Override
    public int insert(String dynamicSQL, String statement) {
        return insert(dynamicSQL, statement, null);
    }

    @Override
    public int insert(String dynamicSQL, String statement, Object parameter) {
        return update(dynamicSQL, statement, parameter);
    }

    @Override
    public int delete(String dynamicSQL, String statement) {
        return delete(dynamicSQL, statement, null);
    }

    @Override
    public int delete(String dynamicSQL, String statement, Object parameter) {
        return update(dynamicSQL, statement, parameter);
    }

    @Override
    public int create(String dynamicSQL, String statement) {
        return update(dynamicSQL, statement);
    }

    @Override
    public int update(String dynamicSQL, String statement) {
        return update(dynamicSQL, statement, null);
    }

    @Override
    public int update(String dynamicSQL, String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            Executor executor = configuration.newExecutor(ms.getCache());
            return executor.update(ms, wrapCollection(parameter),dynamicSQL);
        } catch (Exception e) {
            throw new PersistenceException("Error updating database.  Cause: "
                    + e, e);
        }
    }

    @Override
    public int batchUpdate(String dynamicSQL, String statement, List<Object> parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            Executor executor = configuration.newExecutor(ms.getCache());
            return executor.batchUpdate(ms, parameter,dynamicSQL);
        } catch (Exception e) {
            throw new PersistenceException("Error updating database.  Cause: "
                    + e, e);
        }
    }

    /**
     * 判断参数是否为集合或者数组，如果是将参数放到map当中，
     * 放入方式为；list：map.put("list",object),array:map.put("array",object);
     *
     * @param object
     * @return
     */
    private Object wrapCollection(final Object object) {
        if (object instanceof List) {
            StrictMap<Object> map = new StrictMap<Object>();
            map.put("list", object);
            return map;
        } else if (object != null && object.getClass().isArray()) {
            StrictMap<Object> map = new StrictMap<Object>();
            map.put("array", object);
            return map;
        }
        return object;
    }

    public static class StrictMap<V> extends HashMap<String, V> {
        @Override
        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new BindingException("Parameter '" + key
                        + "' not found. Available parameters are "
                        + this.keySet());
            }
            return super.get(key);
        }

    }
}