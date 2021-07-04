package com.rabbitframework.generator.freemarker;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.core.springframework.io.Resource;
import com.rabbitframework.core.utils.ResourceUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class GeneratorConfig {
	Configuration configuration;
	private static final Logger logger = LoggerFactory.getLogger(GeneratorConfig.class);

	public GeneratorConfig() {
		before();
	}

	public void before() {
		configuration = new Configuration(Configuration.VERSION_2_3_23);
		// configuration.setObjectWrapper(new
		// DefaultObjectWrapper(Configuration.VERSION_2_3_23));
		try {
			// File resource = ResourceUtils.getResourceAsFile("/");
			// logger.debug("path:" + resource.getAbsolutePath());
			StringTemplateLoader templateLoader = new StringTemplateLoader();
			configuration.setTemplateLoader(templateLoader);
			Resource[] resources = ResourceUtils.getResources("classpath*:/template/*.ftl");
			for (Resource resource : resources) {
				String fileName = resource.getFilename();
				String value = IOUtils.toString(resource.getInputStream());
				templateLoader.putTemplate(fileName, value);
			}
			// templateLoader.putTemplate();
			
			// configuration.setDirectoryForTemplateLoading(resource);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void simple() {
		try {
			Template template = configuration.getTemplate("simple.ftl");
			Map map = new HashMap();
			map.put("user", "rabbit");
			template.process(map, new OutputStreamWriter(System.out));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void list() {
		try {
			Template template = configuration.getTemplate("list.ftl");
			List<Model> models = new ArrayList<Model>();
			for (int i = 0; i < 3; i++) {
				Model model = new Model();
				model.setId(i);
				model.setName("list:" + i);
				models.add(model);
			}
			Map map = new HashMap();
			map.put("modelList", models);
			template.process(map, new OutputStreamWriter(System.out));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Properties variables;

	public Properties getVariables() {
		return variables;
	}

	public void setVariables(Properties variables) {
		this.variables = variables;
	}
}
