package com.scloudic.rabbitframework.web.freemarker;

import com.scloudic.rabbitframework.web.annotations.TemplateVariable;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;

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
