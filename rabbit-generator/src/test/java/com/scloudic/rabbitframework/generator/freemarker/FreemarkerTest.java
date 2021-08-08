package com.scloudic.rabbitframework.generator.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.core.utils.ResourceUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {
    Configuration configuration;
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerTest.class);

    @Before
    public void before() {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
//        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        try {
            File resource = ResourceUtils.getResourceAsFile("/template");
            logger.debug("path:" + resource.getAbsolutePath());
            configuration.setDirectoryForTemplateLoading(resource);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void simple() {
        try {
            Template template = configuration.getTemplate("simple.ftl");
            Map<String,Object> map = new HashMap();
            map.put("dialect","mysql");
            map.put("autoincrement",false);
            template.process(map, new OutputStreamWriter(System.out));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

//    @Test
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
}