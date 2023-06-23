package com.scloudic.rabbitframework.jbatis.mapping.binding;

import com.scloudic.rabbitframework.jbatis.annontations.MapKey;
import com.scloudic.rabbitframework.jbatis.annontations.Param;
import com.scloudic.rabbitframework.jbatis.annontations.SQL;
import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.dataaccess.SqlDataAccess;
import com.scloudic.rabbitframework.jbatis.exceptions.BindingException;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * mapper类中方法的具体执行类
 *
 * @author Justin Liang
 */
public class MapperMethod {
    private SqlCommand sqlCommand;
    private MethodSignature methodSignature;

    public MapperMethod(Class<?> mapperInterface, Method method,
                        Configuration configuration) {
        sqlCommand = new SqlCommand(mapperInterface, method, configuration);
        methodSignature = new MethodSignature(configuration, method);
    }

    /**
     * mapper方法执行，通过{@link MapperProxy}中调用
     *
     * @param sqlDataAccess sqlDataAccess
     * @param args          args
     * @return obj
     */
    public Object execute(SqlDataAccess sqlDataAccess, Object[] args) {
        Object result = null;
        String sql = methodSignature.getSQL(args);
        SqlCommendType type = sqlCommand.getCommendType();
        if (SqlCommendType.INSERT == type) {
            Object param = methodSignature.convertArgsToSqlCommandParam(args);
            if (sqlCommand.isBatchUpdate()) {
                if (!List.class.isAssignableFrom(param.getClass())) {
                    throw new IllegalArgumentException(param.getClass() + " is not assignable to " + List.class);
                }
                result = rowCountResult(sqlDataAccess.batchUpdate(sql, sqlCommand.getName(), (List<Object>) param));
            } else {
                result = rowCountResult(sqlDataAccess.insert(sql, sqlCommand.getName(), param));
            }
        } else if (SqlCommendType.UPDATE == type) {
            Object param = methodSignature.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlDataAccess.update(sql, sqlCommand.getName(), param));
        } else if (SqlCommendType.DELETE == type) {
            Object param = methodSignature.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlDataAccess.delete(sql, sqlCommand.getName(), param));
        } else if (SqlCommendType.CREATE == type) {
            result = rowCountResult(sqlDataAccess.create(sql, sqlCommand.getName()));
        } else if (SqlCommendType.SELECT == type) {
            if (methodSignature.isReturnsMany()) {
                result = executeForMany(sqlDataAccess, args, sql);
            } else if (methodSignature.returnsMap()) {
                result = executeForMap(sqlDataAccess, args, sql);
            } else {
                Object param = methodSignature
                        .convertArgsToSqlCommandParam(args);
                result = sqlDataAccess.selectOne(sqlCommand.getName(), param, sql);
            }
        } else {
            throw new BindingException("Unknown execution method for: "
                    + sqlCommand.getName());
        }
        return result;
    }

    private <E> Object executeForMany(SqlDataAccess sqlDataAccess, Object[] args, String sql) {
        List<E> result = null;
        Object param = methodSignature.convertArgsToSqlCommandParam(args);
        if (methodSignature.hasRowBounds()) {
            RowBounds rowBounds = methodSignature.extractRowBounds(args);
            result = sqlDataAccess.selectList(sqlCommand.getName(), param, rowBounds, sql);
        } else {
            result = sqlDataAccess.selectList(sqlCommand.getName(), param, sql);
        }
        if (!methodSignature.getReturnType()
                .isAssignableFrom(result.getClass())) {
            if (methodSignature.getReturnType().isArray()) {
                return convertToArray(result);
            } else {
                return convertToDeclaredCollection(
                        sqlDataAccess.getConfiguration(), result);
            }
        }
        return result;
    }

    private <K, V> Map<K, V> executeForMap(SqlDataAccess sqlDataAccess,
                                           Object[] args, String sql) {
        Map<K, V> result;
        Object param = methodSignature.convertArgsToSqlCommandParam(args);
        if (methodSignature.hasRowBounds()) {
            RowBounds rowBounds = methodSignature.extractRowBounds(args);
            result = sqlDataAccess.<K, V>selectMap(sqlCommand.getName(),
                    param, methodSignature.getMapKey(), rowBounds, sql);
        } else {
            result = sqlDataAccess.<K, V>selectMap(sqlCommand.getName(),
                    param, methodSignature.getMapKey(), sql);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <E> E[] convertToArray(List<E> list) {
        E[] array = (E[]) Array.newInstance(methodSignature.getReturnType()
                .getComponentType(), list.size());
        array = list.toArray(array);
        return array;
    }

    private <E> Object convertToDeclaredCollection(Configuration config,
                                                   List<E> list) {
        Object collection = config.getObjectFactory().create(
                methodSignature.getReturnType());
        MetaObject metaObject = config.newMetaObject(collection);
        metaObject.addAll(list);
        return collection;
    }

    private Object rowCountResult(int rowCount) {
        final Object result;
        if (methodSignature.isReturnsVoid()) {
            result = null;
        } else if (Integer.class.equals(methodSignature.getReturnType())
                || Integer.TYPE.equals(methodSignature.getReturnType())) {
            result = rowCount;
        } else if (Long.class.equals(methodSignature.getReturnType())
                || Long.TYPE.equals(methodSignature.getReturnType())) {
            result = (long) rowCount;
        } else if (Boolean.class.equals(methodSignature.getReturnType())
                || Boolean.TYPE.equals(methodSignature.getReturnType())) {
            result = (rowCount > 0);
        } else {
            throw new BindingException("Mapper method '" + sqlCommand.getName()
                    + "' has an unsupported return type: "
                    + methodSignature.getReturnType());
        }
        return result;
    }

    private static class MethodSignature {
        private final Class<?> returnType;
        private final boolean returnsVoid;
        private final boolean returnsMany;
        private final boolean hasNamedParameters;
        private final Integer rowBoundsIndex;
        private final SortedMap<Integer, String> params;
        private final String mapKey;
        private final boolean returnsMap;
        private final Integer sqlIndex;

        public MethodSignature(Configuration configuration, Method method) {
            returnType = method.getReturnType();
            returnsVoid = void.class.equals(returnType);
            returnsMany = (configuration.getObjectFactory()
                    .isCollection(returnType)) || returnType.isArray();
            mapKey = getMapKey(method);
            this.returnsMap = Map.class
                    .isAssignableFrom(method.getReturnType());
            hasNamedParameters = hasNamedParams(method);
            sqlIndex = getSQLIndex(method);
            rowBoundsIndex = getUniqueParamIndex(method, RowBounds.class);
            params = Collections.unmodifiableSortedMap(getParams(method,
                    hasNamedParameters));

        }

        private String getMapKey(Method method) {
            String mapKey = null;
            if (Map.class.isAssignableFrom(method.getReturnType())) {
                final MapKey mapKeyAnnotation = method
                        .getAnnotation(MapKey.class);
                if (mapKeyAnnotation != null) {
                    mapKey = mapKeyAnnotation.value();
                }
            }
            return mapKey;
        }

        public String getMapKey() {
            return mapKey;
        }

        public boolean returnsMap() {
            return returnsMap;
        }

        public boolean isReturnsMany() {
            return returnsMany;
        }

        public boolean isReturnsVoid() {
            return returnsVoid;
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        public Object convertArgsToSqlCommandParam(Object[] args) {
            final int paramCount = params.size();
            if (args == null || paramCount == 0) {
                return null;
            } else if (!hasNamedParameters && paramCount == 1) {
                return args[params.keySet().iterator().next()];
            } else {
                final Map<String, Object> param = new ParamMap<Object>();
                for (Map.Entry<Integer, String> entry : params.entrySet()) {
                    param.put(entry.getValue(), args[entry.getKey()]);
                }
                return param;
            }
        }

        public boolean hasRowBounds() {
            return (rowBoundsIndex != null);
        }

        public boolean hasDynamicSql() {
            return (sqlIndex != null);
        }

        public String getSQL(Object[] args) {
            return (hasDynamicSql() ? (String) args[sqlIndex] : "");
        }

        public RowBounds extractRowBounds(Object[] args) {
            return (hasRowBounds() ? (RowBounds) args[rowBoundsIndex] : null);
        }

        private SortedMap<Integer, String> getParams(Method method,
                                                     boolean hasNamedParameters) {
            final SortedMap<Integer, String> params = new TreeMap<Integer, String>();
            final Parameter[] parameters = method.getParameters();
            int parametersSize = parameters.length;
            for (int i = 0; i < parametersSize; i++) {
                Parameter parameter = parameters[i];
                SQL selectSQL = parameter.getAnnotation(SQL.class);
                if (selectSQL != null) {
                    continue;
                }
                if (RowBounds.class.isAssignableFrom(parameter.getType())) {
                    continue;
                }
                String paramName = String.valueOf(params.size());
                if (hasNamedParameters) {
                    Param param = parameter.getAnnotation(Param.class);
                    if (param != null) {
                        paramName = param.value();
                    }
                }
                params.put(i, paramName);

            }
            return params;
        }

        private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
            Integer index = null;
            final Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                if (paramType.isAssignableFrom(argTypes[i])) {
                    if (index == null) {
                        index = i;
                    } else {
                        throw new BindingException(method.getName()
                                + " cannot have multiple "
                                + paramType.getSimpleName() + "parameters");
                    }
                }
            }
            return index;
        }

        /**
         * 是否有{@link Param}注解
         *
         * @param method 方法
         * @return boolean
         */
        private boolean hasNamedParams(Method method) {
            boolean hasNameParams = false;
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                if (param != null) {
                    hasNameParams = true;
                    break;
                }
            }
            return hasNameParams;
        }

        private Integer getSQLIndex(Method method) {
            Integer hasSQL = null;
            Parameter[] parameters = method.getParameters();
            int parameterLength = parameters.length;
            for (int i = 0; i < parameterLength; i++) {
                Parameter parameter = parameters[i];
                SQL param = parameter.getAnnotation(SQL.class);
                if (param != null) {
                    hasSQL = i;
                    break;
                }
            }
            return hasSQL;
        }
    }

    public static class ParamMap<V> extends HashMap<String, V> {
        private static final long serialVersionUID = 1L;

        @Override
        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new BindingException("Parameter '" + key
                        + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }
    }

}
