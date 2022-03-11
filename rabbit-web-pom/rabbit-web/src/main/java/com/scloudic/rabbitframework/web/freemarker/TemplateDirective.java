package com.scloudic.rabbitframework.web.freemarker;

import com.scloudic.rabbitframework.core.exceptions.UnKnowException;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class TemplateDirective implements TemplateDirectiveModel {
    private static final Logger logger = LoggerFactory.getLogger(TemplateDirective.class);

    @Override
    public void execute(Environment environment, Map map,
                        TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) {
        try {
            render(environment, map, templateModels, templateDirectiveBody);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UnKnowException(e.getMessage(), e);
        }
    }

    public BeansWrapper getBeansWrapper() {
        DefaultObjectWrapperBuilder builder = new
                DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_29);
        BeansWrapper beansWrapper = builder.build();
        return beansWrapper;
    }


    public abstract void render(Environment environment, Map map,
                                TemplateModel[] templateModels,
                                TemplateDirectiveBody templateDirectiveBody) throws Exception;


}
