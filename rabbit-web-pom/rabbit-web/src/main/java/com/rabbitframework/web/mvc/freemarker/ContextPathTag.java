package com.rabbitframework.web.mvc.freemarker;

import com.rabbitframework.web.annotations.TemplateVariable;
import com.rabbitframework.web.utils.ServletContextHelper;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

//@Component
@TemplateVariable("contextPath")
public class ContextPathTag extends TemplateDirective {

    @Override
    public void render(Environment environment, Map map,
                       TemplateModel[] templateModels,
                       TemplateDirectiveBody templateDirectiveBody) throws Exception {
        BeansWrapper beansWrapper = getBeansWrapper();
        TemplateModel templateModel = beansWrapper.
                wrap(ServletContextHelper.getServletContext().getContextPath());
        environment.setVariable("basePath", templateModel);
        templateDirectiveBody.render(environment.getOut());
    }
}
