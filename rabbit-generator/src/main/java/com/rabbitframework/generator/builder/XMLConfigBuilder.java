package com.rabbitframework.generator.builder;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.generator.dataaccess.JdbcConnectionInfo;
import com.rabbitframework.generator.exceptions.BuilderException;
import com.rabbitframework.generator.template.JavaModeGenerate;
import com.rabbitframework.generator.template.Template;
import com.tjzq.commons.propertytoken.PropertyParser;
import com.tjzq.commons.utils.ResourceUtils;
import com.tjzq.commons.utils.StringUtils;
import com.tjzq.commons.xmlparser.XNode;
import com.tjzq.commons.xmlparser.XPathParser;

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
			jdbcConnectionElement(root.evalNode("jdbcConnection"));
			generatorsElement(root.evalNode("generators"));
			tablesElement(root.evalNode("tables"));
		} catch (Exception e) {
			logger.error("Error parsing generator Configuration. Cause: " + e, e);
			throw new BuilderException("Error parsing generator Configuration. Cause: " + e, e);
		}
	}

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

	private void jdbcConnectionElement(XNode jdbcConnection) throws Exception {
		if (jdbcConnection == null) {
			return;
		}

		JdbcConnectionInfo jdbcConnectionInfo = new JdbcConnectionInfo();
		Properties variables = configuration.getVariables();
		String driverClass = jdbcConnection.getStringAttribute("driverClass");
		String catalog = jdbcConnection.getStringAttribute("catalog");
		String connectionURL = jdbcConnection.getStringAttribute("connectionURL");
		String userName = jdbcConnection.getStringAttribute("userName");
		String password = jdbcConnection.getStringAttribute("password");
		Properties properties = jdbcConnection.getChildrenAsProperties();
		driverClass = PropertyParser.parseDollar(driverClass, variables);
		catalog = PropertyParser.parseDollar(catalog, variables);
		connectionURL = PropertyParser.parseDollar(connectionURL, variables);
		userName = PropertyParser.parseDollar(userName, variables);
		password = PropertyParser.parseDollar(password, variables);
		jdbcConnectionInfo.setCatalog(catalog);
		jdbcConnectionInfo.setConnectionURL(connectionURL);
		jdbcConnectionInfo.setUserName(userName);
		jdbcConnectionInfo.setPassword(password);
		jdbcConnectionInfo.setDriverClass(driverClass);
		if (properties != null) {
			jdbcConnectionInfo.setProperties(properties);
		}
		configuration.setJdbcConnectionInfo(jdbcConnectionInfo);
	}

	private void generatorsElement(XNode generators) {
		if (generators == null) {
			return;
		}
		Template template = new Template();
		List<XNode> xnode = generators.evalNodes("generator");
		for (XNode cXnode : xnode) {
			JavaModeGenerate javaModeGenerate = new JavaModeGenerate();
			String templatePath = cXnode.getStringAttribute("templatePath");
			String targetPackage = cXnode.getStringAttribute("targetPackage");
			String targetProject = cXnode.getStringAttribute("targetProject");
			String fileSuffix = cXnode.getStringAttribute("fileSuffix");
			String extension = cXnode.getStringAttribute("extension", ".java");
			javaModeGenerate.setTargetPackage(targetPackage);
			javaModeGenerate.setTargetProject(targetProject);
			javaModeGenerate.setTemplatePath(templatePath);
			javaModeGenerate.setFileSuffix(fileSuffix);
			javaModeGenerate.setExtension(extension);
			template.put(javaModeGenerate);
		}
		configuration.setTemplate(template);
	}

	private void tablesElement(XNode tables) {
		if (tables == null) {
			return;
		}
		TableConfiguration tableConfiguration = new TableConfiguration();
		String tableType = tables.getStringAttribute("type", TableType.ALL.getType());
		String filter = tables.getStringAttribute("filter", "");
		if (tableType.equals(TableType.ASSIGN.getType())) {
			List<XNode> tableNodes = tables.evalNodes("table");
			if (tableNodes.size() > 0) {
				for (XNode tableNode : tableNodes) {
					String tableName = tableNode.getStringAttribute("tableName");
					String objectName = tableNode.getStringAttribute("objectName");
					tableConfiguration.put(tableName, objectName);
				}
			} else {
				tableType = TableType.ALL.getType();
			}
		}
		tableConfiguration.setTableType(TableType.getTableType(tableType));
		tableConfiguration.setFilter(filter);
		configuration.setTableConfiguration(tableConfiguration);
	}
}
