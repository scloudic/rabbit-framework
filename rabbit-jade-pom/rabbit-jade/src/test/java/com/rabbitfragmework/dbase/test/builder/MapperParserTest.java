package com.rabbitfragmework.dbase.test.builder;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rabbitfragmework.dbase.test.mapper.TestUserMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.jade.annontations.SQLProvider;
import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.builder.MapperParser;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.mapping.SqlCommendType;

public class MapperParserTest {
	private static final Logger logger = LoggerFactory.getLogger(MapperParserTest.class);

	public static void main(String[] args) {
		String sqlValueSrc = "dddd$${whereParamsType}";
		Pattern p = Pattern.compile("\\$\\$\\{(.*?)\\}");
		Matcher m = p.matcher(sqlValueSrc);
		String regex = "(.*?)*\\$\\$\\{(.*?)\\}*";
		boolean b = sqlValueSrc.matches(regex);
		System.out.println(Object.class.getSimpleName().equals(String.class.getSimpleName()));
	}

	// @Test
	public void testMapperParser() {
		Configuration configuration = new Configuration();
		MapperParser mapperParser = new MapperParser(configuration, TestUserMapper.class);
		mapperParser.parse();
		Collection<MappedStatement> mappedStatements = configuration.getMappedStatements();
		logger.debug("mappedStatements.size():" + mappedStatements.size());
		Iterator<MappedStatement> mappedStatementIterator = mappedStatements.iterator();
		while (mappedStatementIterator.hasNext()) {
			MappedStatement mappedStatement = mappedStatementIterator.next();
			logger.debug("id:" + mappedStatement.getId());
		}
	}

	// @Test
	public void testMapperSQLProvider() throws Exception {
		Method method = TestUserMapper.class.getMethod("insertTest", null);
		SQLProvider sqlProvider = method.getAnnotation(SQLProvider.class);
		Class<?> typeClazz = sqlProvider.type();
		String methodName = sqlProvider.method();
		Method methodAnn = typeClazz.getMethod(methodName, null);
		String string = (String) methodAnn.invoke(typeClazz.newInstance());
		logger.debug("value:" + string);
	}

	@Test
	public void testMapperType() throws Exception {
		Method method = TestUserMapper.class.getMethod("selectTest", null);
		Class<?> result = getReturnType(method);
		System.out.println("result:" + result);
	}

	private Class<?> getReturnType(Method method) {
		Class<?> returnType = method.getReturnType();
		if (void.class.equals(returnType)) {
			return returnType;
		} else if (Collection.class.isAssignableFrom(returnType)) {
			Type returnTypeParameter = method.getGenericReturnType();
			if (returnTypeParameter instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
				if (actualTypeArguments != null && actualTypeArguments.length == 1) {
					returnTypeParameter = actualTypeArguments[0];
					if (returnTypeParameter instanceof Class) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
					} else if (returnTypeParameter instanceof GenericArrayType) {
						Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
								.getGenericComponentType();
						returnType = Array.newInstance(componentType, 0).getClass();
					}
				}
			}
		} else if (Map.class.isAssignableFrom(returnType)) {
			Type returnTypeParameter = method.getGenericReturnType();
			if (returnTypeParameter instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
				if (actualTypeArguments != null && actualTypeArguments.length == 2) {
					returnTypeParameter = actualTypeArguments[1];
					if (returnTypeParameter instanceof Class) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
					}
				}
			}
		}
		return returnType;
	}

	private SqlCommendType getSQLCommendType(String sqlValue) {
		SqlCommendType sqlCommendType = SqlCommendType.UNKNOWN;
		for (int i = 0; i < SELECT_PATTERNS.length; i++) {
			if (SELECT_PATTERNS[i].matcher(sqlValue).find()) {
				sqlCommendType = SqlCommendType.SELECT;
				break;
			}
		}
		if (sqlCommendType == SqlCommendType.UNKNOWN) {
			sqlCommendType = SqlCommendType.INSERT;
		}
		return sqlCommendType;
	}

	private static Pattern[] SELECT_PATTERNS = new Pattern[] {
			Pattern.compile("^\\s*SELECT.*", Pattern.CASE_INSENSITIVE) };
}
