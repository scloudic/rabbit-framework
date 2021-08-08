package com.scloudic.rabbitframework.jbatis.mapping.binding;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.scloudic.rabbitframework.jbatis.reflect.MetaObject;
import com.scloudic.rabbitframework.jbatis.annontations.MapKey;
import com.scloudic.rabbitframework.jbatis.annontations.Param;
import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.dataaccess.SqlDataAccess;
import com.scloudic.rabbitframework.jbatis.exceptions.BindingException;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;

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
     * @param args
     * @return
     */
    public Object execute(SqlDataAccess sqlDataAccess, Object[] args) {
        Object result = null;
        SqlCommendType type = sqlCommand.getCommendType();
        if (SqlCommendType.INSERT == type) {
            Object param = methodSignature.convertArgsToSqlCommandParam(args);
            if (sqlCommand.isBatchUpdate()) {
                if (!List.class.isAssignableFrom(param.getClass())) {
                    throw new IllegalArgumentException(param.getClass() + " is not assignable to " + List.class);
                }
                result = rowCountResult(sqlDataAccess.batchUpdate(sqlCommand.getName(),
                        (List<Object>) param));
            } else {
                result = rowCountResult(sqlDataAccess.insert(sqlCommand.getName(),
                        param));
            }
        } else if (SqlCommendType.UPDATE == type) {
            Object param = methodSignature.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlDataAccess.update(sqlCommand.getName(),
                    param));
        } else if (SqlCommendType.DELETE == type) {
            Object param = methodSignature.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlDataAccess.delete(sqlCommand.getName(),
                    param));
        } else if (SqlCommendType.CREATE == type) {
            result = rowCountResult(sqlDataAccess.create(sqlCommand.getName()));
        } else if (SqlCommendType.SELECT == type) {
            if (methodSignature.isReturnsMany()) {
                result = executeForMany(sqlDataAccess, args);
            } else if (methodSignature.returnsMap()) {
                result = executeForMap(sqlDataAccess, args);
            } else {
                Object param = methodSignature
                        .convertArgsToSqlCommandParam(args);
                result = sqlDataAccess.selectOne(sqlCommand.getName(), param);
            }
        } else {
            throw new BindingException("Unknown execution method for: "
                    + sqlCommand.getName());
        }
        return result;
    }

    private <E> Object executeForMany(SqlDataAccess sqlDataAccess, Object[] args) {
        List<E> result = null;
        Object param = methodSignature.convertArgsToSqlCommandParam(args);
        if (methodSignature.hasRowBounds()) {
            RowBounds rowBounds = methodSignature.extractRowBounds(args);
            result = sqlDataAccess.<E>selectList(sqlCommand.getName(), param,
                    rowBounds);
        } else {
            result = sqlDataAccess.<E>selectList(sqlCommand.getName(), param);
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
                                           Object[] args) {
        Map<K, V> result;
        Object param = methodSignature.convertArgsToSqlCommandParam(args);
        if (methodSignature.hasRowBounds()) {
            RowBounds rowBounds = methodSignature.extractRowBounds(args);
            result = sqlDataAccess.<K, V>selectMap(sqlCommand.getName(),
                    param, methodSignature.getMapKey(), rowBounds);
        } else {
            result = sqlDataAccess.<K, V>selectMap(sqlCommand.getName(),
                    param, methodSignature.getMapKey());
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

        public MethodSignature(Configuration configuration, Method method) {
            returnType = method.getReturnType();
            returnsVoid = void.class.equals(returnType);
            returnsMany = (configuration.getObjectFactory()
                    .isCollection(returnType)) || returnType.isArray();
            mapKey = getMapKey(method);
            this.returnsMap = Map.class
                    .isAssignableFrom(method.getReturnType());
            hasNamedParameters = hasNamedParams(method);
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

        public RowBounds extractRowBounds(Object[] args) {
            return (hasRowBounds() ? (RowBounds) args[rowBoundsIndex] : null);
        }

        private SortedMap<Integer, String> getParams(Method method,
                                                     boolean hasNamedParameters) {
            final SortedMap<Integer, String> params = new TreeMap<Integer, String>();
            final Class<?>[] argTypes = method.getParameterTypes();

            for (int i = 0; i < argTypes.length; i++) {
                if (!RowBounds.class.isAssignableFrom(argTypes[i])) {
                    String paramName = String.valueOf(params.size());
                    if (hasNamedParameters) {
                        paramName = getParamNameFormAnnotation(method, i,
                                paramName);
                    }
                    params.put(i, paramName);
                }
            }
            return params;
        }

        private String getParamNameFormAnnotation(Method method, int i,
                                                  String paramName) {
            final Object[] paramAnnos = method.getParameterAnnotations()[i];
            for (Object paramAnno : paramAnnos) {
                if (paramAnno instanceof Param) {
                    paramName = ((Param) paramAnno).value();
                }
            }
            return paramName;
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
         * @param method
         * @return
         */
        private boolean hasNamedParams(Method method) {
            boolean hasNameParams = false;
            final Object[][] paramAnnos = method.getParameterAnnotations();
            for (Object[] paramAnno : paramAnnos) {
                for (Object aParamAnno : paramAnno) {
                    if (aParamAnno instanceof Param) {
                        hasNameParams = true;
                        break;
                    }
                }
            }
            return hasNameParams;
        }
    }

    private static class SqlCommand {
        private String name;
        private SqlCommendType commendType;
        private boolean batchUpdate = false;

        public SqlCommand(Class<?> mapperInterface, Method method,
                          Configuration configuration) {
            String statementName = mapperInterface.getName() + "."
                    + method.getName();
            MappedStatement ms = null;
            if (configuration.hasStatement(statementName)) {
                ms = configuration.getMappedStatement(statementName);
            } else if (!mapperInterface.equals(method.getDeclaringClass()
                    .getName())) {
                String parentStatementName = method.getDeclaringClass()
                        .getName() + "." + method.getName();
                if (configuration.hasStatement(parentStatementName)) {
                    ms = configuration.getMappedStatement(parentStatementName);
                }
            }
            if (ms == null) {
                throw new BindingException(
                        "Invalid bound statement (not found): " + statementName);
            }
            name = ms.getId();
            commendType = ms.getSqlCommendType();
            batchUpdate = ms.isBatchUpdate();
            if (commendType == SqlCommendType.UNKNOWN) {
                throw new BindingException("Unknown execution method for: "
                        + name);
            }
        }

        public String getName() {
            return name;
        }

        public boolean isBatchUpdate() {
            return batchUpdate;
        }

        public SqlCommendType getCommendType() {
            return commendType;
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
