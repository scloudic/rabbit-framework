package com.rabbitframework.jade.builder;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.rabbitframework.jade.dataaccess.DataSourceBean;
import com.rabbitframework.jade.dataaccess.Environment;
import com.rabbitframework.jade.exceptions.BuilderException;
import com.rabbitframework.jade.intercept.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.jade.cache.Cache;
import com.rabbitframework.jade.cache.CacheBuilder;
import com.rabbitframework.jade.dataaccess.datasource.DataSourceFactory;
import com.tjzq.commons.utils.ClassUtils;
import com.tjzq.commons.utils.ResourceUtils;
import com.tjzq.commons.utils.StringUtils;
import com.tjzq.commons.xmlparser.XNode;
import com.tjzq.commons.xmlparser.XPathParser;

/**
 * 构建启动xml初始化
 */
public class XMLConfigBuilder extends BaseBuilder {
	private static final Logger logger = LoggerFactory.getLogger(XMLConfigBuilder.class);
	private boolean parsed;
	private XPathParser xPathParser;

	public XMLConfigBuilder(Reader reader) {
		this(reader, null);
	}

	public XMLConfigBuilder(Reader reader, Properties properties) {
		this(new XPathParser(reader, false, properties, null), properties);
	}

	public XMLConfigBuilder(InputStream inputStream) {
		this(inputStream, null);
	}

	public XMLConfigBuilder(InputStream inputStream, Properties properties) {
		this(new XPathParser(inputStream, false, properties, null), properties);
	}

	private XMLConfigBuilder(XPathParser xPathParser, Properties pro) {
		super(new Configuration());
		this.parsed = false;
		this.xPathParser = xPathParser;
		configuration.setVariables(pro);
	}

	/**
	 * xmlconfig解析
	 *
	 * @return
	 */
	public Configuration parse() {
		if (parsed) {
			logger.error("Each XMLConfigBuilder can only be used once.");
			throw new BuilderException("Each XMLConfigBuilder can only be used once.");
		}
		parsed = true;
		parseConfiguration(xPathParser.evalNode("/configuration"));
		return configuration;
	}

	private void parseConfiguration(XNode root) {
		try {
			propertiesElement(root.evalNode("properties"));
			pluginElement(root.evalNode("plugins"));
			cachesElements(root.evalNode("caches"));
			dataAccessElement(root.evalNode("dataAccess"));
			entityElement(root.evalNode("entitys"));
			mapperElement(root.evalNode("mappers"));
		} catch (Exception e) {
			logger.error("Error parsing SQL Mapper Configuration. Cause: " + e, e);
			throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
		}
	}

	/**
	 * properties元素解析
	 *
	 * @param pro
	 */
	private void propertiesElement(XNode pro) throws Exception {
		if (pro == null) {
			return;
		}
		Properties properties = pro.getChildrenAsProperties();
		String resource = pro.getStringAttribute("resource");
		if (StringUtils.isNotBlank(resource)) {
			Properties propertiesResource = ResourceUtils.getResourceAsProperties(resource);
			properties.putAll(propertiesResource);
		}
		Properties variables = configuration.getVariables();
		if (variables != null) {
			properties.putAll(variables);
		}
		xPathParser.setVariables(properties);
		configuration.setVariables(properties);
	}

	/**
	 * plugin元素解析,plugin主要作用是添加拦截器
	 *
	 * @param pluginNode
	 * @throws Exception
	 */
	private void pluginElement(XNode pluginNode) throws Exception {
		if (pluginNode == null) {
			return;
		}
		List<XNode> childrenPluginNode = pluginNode.getChildren();
		int size = childrenPluginNode.size();
		for (int i = 0; i < size; i++) {
			XNode cxnode = childrenPluginNode.get(i);
			String interceptorStr = cxnode.getStringAttribute("interceptor");
			Interceptor interceptor = (Interceptor) resolveClass(interceptorStr).newInstance();
			configuration.addInterceptor(interceptor);
		}
	}

	@SuppressWarnings("unchecked")
	private void cachesElements(XNode cachesNode) throws Exception {
		if (cachesNode == null) {
			return;
		}
		List<XNode> cacheList = cachesNode.evalNodes("cache");
		Properties variables = configuration.getVariables();
		for (XNode xNode : cacheList) {
			String name = xNode.getStringAttribute("name");
			String classStr = xNode.getStringAttribute("class");
			Properties properties = xNode.getChildrenAsProperties();
			CacheBuilder cacheBuilder = new CacheBuilder(name);
			Class<? extends Cache> cache = (Class<? extends Cache>) resolveClass(classStr);
			cacheBuilder.implementation(cache);
			Cache cacheObj = cacheBuilder.builder();
			PropertiesConvert.setProperties(properties, cacheObj, variables);
			configuration.addCache(name, cacheObj);
		}
	}

	private void dataAccessElement(XNode dataAccessNode) throws Exception {
		if (dataAccessNode == null) {
			return;
		}
		XNode dataSourceFactoryNode = dataAccessNode.evalNode("dataSourceFactory");
		if (dataSourceFactoryNode == null) {
			throw new NullPointerException("dataSourceFactory is null");
		}
		String dsFactoryClazz = dataSourceFactoryNode.getStringAttribute("class");
		DataSourceFactory dataSourceFactory = (DataSourceFactory) resolveClass(dsFactoryClazz).newInstance();
		dataSourceElement(dataAccessNode, dataSourceFactory);
	}

	private void dataSourceElement(XNode dataAccessNode, DataSourceFactory dataSourceFactory) throws Exception {
		List<XNode> dataSources = dataAccessNode.evalNode("dataSources").getChildren();
		if (dataSources.size() == 0) {
			return;
		}
		Environment environment = new Environment();
		Properties variables = configuration.getVariables();
		for (XNode xNode : dataSources) {
			String name = xNode.getStringAttribute("name");
			String dataSourceClazz = xNode.getStringAttribute("class");
			String dialectStr = xNode.getStringAttribute("dialect");
			Properties properties = xNode.getChildrenAsProperties();
			DataSource dataSource = (DataSource) resolveClass(dataSourceClazz).newInstance();
			PropertiesConvert.setProperties(properties, dataSource, variables);
			DataSourceBean dataSourceBean = new DataSourceBean();
			dataSourceBean.setDataSource(dataSource);
			dataSourceBean.setDialect(dialectStr);
			dataSourceFactory.addDataSource(name, dataSourceBean);
			environment.addCacheDataSource(dataSource);
		}
		environment.setDataSourceFactory(dataSourceFactory);
		configuration.setEnvironment(environment);
	}

	private void entityElement(XNode context) {
		if (context != null) {
			List<XNode> packageNodes = context.evalNodes("package");
			for (XNode xNode : packageNodes) {
				String packageName = xNode.getStringAttribute("name");
				String[] packageNames = StringUtils.tokenizeToStringArray(packageName);
				configuration.addEntitys(packageNames);
			}
			List<XNode> entityNodes = context.evalNodes("entity");
			for (XNode xNode : entityNodes) {
				String entityClassName = xNode.getStringAttribute("class");
				Class<?> entityClass = ClassUtils.classForName(entityClassName);
				configuration.addEntity(entityClass);
			}
		}
	}

	private void mapperElement(XNode context) {
		if (context != null) {
			List<XNode> packageNodes = context.evalNodes("package");
			for (XNode xNode : packageNodes) {
				String packageName = xNode.getStringAttribute("name");
				String[] packageNames = StringUtils.tokenizeToStringArray(packageName);
				configuration.addMappers(packageNames);
			}

			List<XNode> mapperNodes = context.evalNodes("mapper");
			for (XNode xNode : mapperNodes) {
				String mapperClass = xNode.getStringAttribute("class");
				Class<?> mapperInteface = ClassUtils.classForName(mapperClass);
				configuration.addMapper(mapperInteface);
			}
		}
	}
}
