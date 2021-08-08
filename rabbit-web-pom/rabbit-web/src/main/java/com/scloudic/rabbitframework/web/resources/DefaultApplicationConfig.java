/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scloudic.rabbitframework.web.resources;

import com.scloudic.rabbitframework.web.exceptions.ExceptionMapperSupport;
import com.scloudic.rabbitframework.web.filter.XSSFilter;
import com.scloudic.rabbitframework.web.mvc.freemarker.FreemarkerMvcFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class DefaultApplicationConfig extends ApplicationConfig {
    public DefaultApplicationConfig() {
        super();
        register(RequestContextFilter.class);
        register(ExceptionMapperSupport.class);
        register(FreemarkerMvcFeature.class);
        register(MultiPartFeature.class);
        register(JspMvcFeature.class);
        register(XSSFilter.class);
    }


}
