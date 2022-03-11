package com.scloudic.rabbitframework.example.template;

import com.scloudic.rabbitframework.web.freemarker.TemplateDirective;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

//@Component
//@TemplateVariable("contextPath")
public class TestContextPathTag extends TemplateDirective {

    @Override
    public void render(Environment environment, Map map,
                       TemplateModel[] templateModels,
                       TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        Writer writer = environment.getOut();
        System.out.println("项目目录：" + ServletContextHelper.getServletContext().getContextPath());
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_29);
        TemplateModel templateModel = builder.build().wrap("测试结果");
         environment.setVariable("value", templateModel);
        writer.write(ServletContextHelper.getServletContext().getContextPath());
        if (templateDirectiveBody != null) {
            templateDirectiveBody.render(writer);
        }
    }
}
