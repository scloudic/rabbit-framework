package com.scloudic.rabbitfragmework.jbatis.test.builder;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.builder.XMLConfigBuilder;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.core.utils.ResourceUtils;

import junit.framework.TestCase;

/**
 * xml初始化测试
 *
 * @author Justin
 */
public class ConfigBuilderTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(ConfigBuilderTest.class);

	public void testConfig() throws IOException {
		Reader reader = ResourceUtils.getResourceAsReader("dbaseConfig.xml");
		XMLConfigBuilder configBuilder = new XMLConfigBuilder(reader);
		Configuration configuration = configBuilder.parse();
		reader.close();
		logger.debug(configuration.getVariables().size() + "");
		Collection<EntityMap> entityMaps = configuration.getEntityRegistry().getEntityMaps();
		for (EntityMap entityMap : entityMaps) {
			logger.debug(entityMap.getIdProperties().size() + "");
			logger.debug(entityMap.getColumnProperties().size() + "");
		}
	}

	// public static void main(String[] args) throws Exception {
	// Class c = UserTestExtend.class;
	// Field field = c.getDeclaredField("userName");
	//
	// Column column = field.getAnnotation(Column.class);
	// System.out.println(column.name());

	// Field[] f = c.getDeclaredFields();
	// for (int i = 0; i < f.length; i++) {
	// System.out.println(f[i].getName());
	// }
	// DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
	// DefaultDialectType defaultDialectType =
	// DefaultDialectType.valueOf("ORACLE");
	// Dialect dialect = defaultDialectType.getInstance();
	// System.out.println(dialect.toString());

	// Resource r =
	// ResourceUtil.getResource("classpath:com/easybatis/anntest/entity/*.java");
	// System.out.println(r.getFile());
	// System.out.println(File.separator);
	// StringBuilder sbPrefix = new StringBuilder();
	// StringBuilder sbSuffix = new StringBuilder();
	// String property = "userid";
	// String column = "user_id";
	// sbPrefix.append("<if test=\""+property+" != null\" >");
	// sbPrefix.append("\n");
	// sbPrefix.append(column);
	// sbPrefix.append(",");
	// sbPrefix.append("</if>");
	// System.out.println(sbPrefix.toString());
	// System.out.println(ClassUtils.getClassLoader().getResource("."));
	// }
}