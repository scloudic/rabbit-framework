package com.scloudic.rabbitframework.web.mvc.freemarker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;

import com.scloudic.rabbitframework.web.annotations.TemplateVariable;
import com.scloudic.rabbitframework.web.exceptions.ResourceException;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * freemarker初始化工厂类
 * <p/>
 * 3.3.1版本：增加定自义模板加载
 *
 * @author justin
 * @updateVersion 3.3.1
 */
public class FreemarkerDefaultConfigurationFactory implements FreemarkerConfigurationFactory {
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerDefaultConfigurationFactory.class);

    protected final Configuration configuration;

    public FreemarkerDefaultConfigurationFactory(ServletContext servletContext, String templateVariablePath) {
        super();
        // Create different loaders.
        final List<TemplateLoader> loaders = new ArrayList<>();
        if (servletContext != null) {
            loaders.add(new WebappTemplateLoader(servletContext));
        }
        loaders.add(new ClassTemplateLoader(FreemarkerDefaultConfigurationFactory.class, "/"));
        try {
            loaders.add(new FileTemplateLoader(new File("/")));
        } catch (IOException e) {
            // NOOP
        }
        configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
        setSharedVariable(templateVariablePath);

    }

    public FreemarkerDefaultConfigurationFactory(Configuration configuration, String templateVariablePath) {
        super();
        this.configuration = configuration;
        setSharedVariable(templateVariablePath);
    }

    private void setSharedVariable(String templateVariablePath) {
        configuration.setSharedVariable("contextPath", new ContextPathTag());
        if (StringUtils.isBlank(templateVariablePath)) {
            return;
        }
        try {
            List<Class<?>> classes = ClassUtils.getClassNamePackage(StringUtils.tokenizeToStringArray(templateVariablePath));
            int classSize = classes.size();
            for (int i = 0; i < classSize; i++) {
                Class<?> clazz = classes.get(i);
                TemplateVariable templateVariable = clazz.getAnnotation(TemplateVariable.class);
                if (templateVariable == null) {
                    continue;
                }
                String name = templateVariable.value();
                Object object = null;
                try {
                    object = ServletContextHelper.getBean(clazz);
                } catch (Exception e) {
                    //忽略此处错误
                }
                if (object == null) {
                    object = ClassUtils.newInstance(clazz);
                }
                configuration.setSharedVariable(name, object);
            }
        } catch (Exception e) {
            throw new ResourceException("load freemarker template variable error");
        }
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}
