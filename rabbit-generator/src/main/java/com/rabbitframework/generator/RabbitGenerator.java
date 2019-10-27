package com.rabbitframework.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.generator.builder.Configuration;
import com.rabbitframework.generator.dataaccess.ConnectionFactory;
import com.rabbitframework.generator.dataaccess.DatabaseIntrospector;
import com.rabbitframework.generator.exceptions.GeneratorException;
import com.rabbitframework.generator.mapping.EntityMapping;
import com.rabbitframework.generator.template.JavaModeGenerate;
import com.rabbitframework.generator.template.Template;
import com.rabbitframework.generator.utils.Constants;

public class RabbitGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RabbitGenerator.class);
    private Configuration configuration;

    public RabbitGenerator(Configuration configuration) {
        this.configuration = configuration;
    }

    public void generator() {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection(configuration.getJdbcConnectionInfo());
            DatabaseMetaData metaData = connection.getMetaData();
            DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(metaData, configuration);
            List<EntityMapping> entityMappings = databaseIntrospector.introspectTables();
            Template template = configuration.getTemplate();
            Map<String, JavaModeGenerate> templateMappingMap = template.getTemplateMapping();
            Map<String, Object> outMap = new HashMap<String, Object>();
            String modelPackage = configuration.getVariables().getProperty(Constants.MODEL_PACKAGE);
            modelPackage = modelPackage == null ? "" : modelPackage;
            outMap.put(Constants.MODEL_PACKAGE, modelPackage);
            for (Map.Entry<String, JavaModeGenerate> entry : templateMappingMap.entrySet()) {
                String key = entry.getKey();
                JavaModeGenerate javaModeGenerate = entry.getValue();
                String packageName = javaModeGenerate.getTargetPackage();
                String outPath = javaModeGenerate.getTargetProject();
                String fileSuffix = javaModeGenerate.getFileSuffix();
                String extension = javaModeGenerate.getExtension();
                outMap.put(Constants.PACKAGE_NAME_KEY, packageName);
                outMap.put(Constants.FILE_SUFFIX_KEY, fileSuffix);
                for (EntityMapping entityMapping : entityMappings) {
                    outMap.put(Constants.ENTITY_KEY, entityMapping);
                    String fileName = entityMapping.getObjectName() + fileSuffix + extension;
//                    template.printToConsole(outMap, key);
                    template.printToFile(outMap, key, outPath, fileName);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GeneratorException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
