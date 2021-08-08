package com.scloudic.rabbitframework.generator.builder;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.scloudic.rabbitframework.core.xmlparser.XNode;
import com.scloudic.rabbitframework.core.xmlparser.XPathParser;
import com.scloudic.rabbitframework.generator.dataaccess.JdbcConnectionInfo;
import com.scloudic.rabbitframework.generator.exceptions.BuilderException;
import com.scloudic.rabbitframework.generator.template.JavaModeGenerate;
import com.scloudic.rabbitframework.generator.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.core.propertytoken.PropertyParser;
import com.scloudic.rabbitframework.core.utils.ResourceUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;

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
            for (Map.Entry entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                String result = PropertyParser.parseDollar(value, propertiesResource);
                properties.put(key, result);
            }

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
        String dialect = jdbcConnection.getStringAttribute("dialect", "mysql");
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
        jdbcConnectionInfo.setDialect(dialect);
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
        String parentPackage = generators.getStringAttribute("parentPackage");
        String targetPath = generators.getStringAttribute("targetPath");
        String service = generators.getStringAttribute("service", "");
        String entity = generators.getStringAttribute("entity", "entity");
        String mapper = generators.getStringAttribute("mapper", "mapper");
        String fileSuffix = "";
        template.put(getJavaModeGenerate(parentPackage + "." + entity, targetPath,
                "template/model.ftl", parentPackage, service, mapper, entity, fileSuffix));

        fileSuffix = mapper.substring(0, 1).toUpperCase() + mapper.substring(1);
        template.put(getJavaModeGenerate(parentPackage + "." + mapper, targetPath,
                "template/mapper.ftl", parentPackage, service, mapper, entity, fileSuffix));

        if (StringUtils.isNotBlank(service)) {
            fileSuffix = service.substring(0, 1).toUpperCase() + service.substring(1);
            template.put(getJavaModeGenerate(parentPackage + "." + service, targetPath,
                    "template/service.ftl", parentPackage, service, mapper, entity, fileSuffix));
            template.put(getJavaModeGenerate(parentPackage + "." + service + ".impl", targetPath,
                    "template/serviceImpl.ftl", parentPackage, service, mapper, entity, fileSuffix + "Impl"));
        }
        configuration.setTemplate(template);
    }

    private JavaModeGenerate getJavaModeGenerate(String targetPackage, String targetPath,
                                                 String templatePath, String parentPackage,
                                                 String serviceSuffix, String mapperSuffix, String entitySuffix, String fileSuffix) {
        JavaModeGenerate javaModeGenerate = new JavaModeGenerate();
        javaModeGenerate.setTargetPackage(targetPackage);
        javaModeGenerate.setTargetProject(targetPath);
        javaModeGenerate.setTemplatePath(templatePath);
        javaModeGenerate.setMapperSuffix(mapperSuffix);
        javaModeGenerate.setServiceSuffix(serviceSuffix);
        javaModeGenerate.setEntitySuffix(entitySuffix);
        javaModeGenerate.setExtension(".java");
        javaModeGenerate.setParentPackage(parentPackage);
        javaModeGenerate.setFileSuffix(fileSuffix);
        return javaModeGenerate;
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
