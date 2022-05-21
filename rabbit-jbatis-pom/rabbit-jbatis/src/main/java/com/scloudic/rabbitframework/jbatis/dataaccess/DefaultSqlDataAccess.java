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
    public <T> T selectOne(String statement) {
        return selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = selectList(statement, parameter);
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
    public <E> List<E> selectList(String statement) {
        return selectList(statement, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return selectList(statement, parameter, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter,
                                  RowBounds rowBounds) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            Object obj = wrapCollection(parameter);
            Executor executor = configuration.newExecutor(ms.getCache());
            return executor.query(ms, obj, rowBounds);
        } catch (Exception e) {
            throw new PersistenceException("Error querying database.  Cause: "
                    + e, e);
        }

    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return this.selectMap(statement, null, mapKey, null);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter,
                                      String mapKey) {
        return this.selectMap(statement, parameter, mapKey, null);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> selectMap(String statement, Object parameter,
                                      String mapKey, RowBounds rowBounds) {
        final List<?> list = selectList(statement, parameter, rowBounds);
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
    public int insert(String statement) {
        return insert(statement, null);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override
    public int delete(String statement) {
        return delete(statement, null);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override
    public int create(String statement) {
        return update(statement);
    }

    @Override
    public int update(String statement) {
        return update(statement, null);
    }

    @Override
    public int update(String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            Executor executor = configuration.newExecutor(ms.getCache());
            return executor.update(ms, wrapCollection(parameter));
        } catch (Exception e) {
            throw new PersistenceException("Error updating database.  Cause: "
                    + e, e);
        }
    }

    @Override
    public int batchUpdate(String statement, List<Object> parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            Executor executor = configuration.newExecutor(ms.getCache());
            return executor.batchUpdate(ms, parameter);
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