package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.core.reflect.MetaClass;
import com.scloudic.rabbitframework.example.web.biz.TestBiz;
import com.scloudic.rabbitframework.example.web.rest.Test;
import com.scloudic.rabbitframework.web.AbstractContextResource;
import com.scloudic.rabbitframework.web.Result;
import com.scloudic.rabbitframework.web.annotations.FormValid;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;

@Component
@Singleton
@Path("/test")
public class TestResource extends AbstractContextResource {
    private static final Logger logger = LoggerFactory.getLogger(TestResource.class);
    @Autowired
    private TestBiz testBiz;

    @GET
    @Path("getData")
    public Object getData() {
        logger.debug("getData");
        return getSimpleResponse(true);
    }

    @GET
    @Path("getParams")
    @FormValid
    public Object getParams(@NotBlank(message = "{name.null}") @QueryParam("name") String name) {
        String value = testBiz.test(name);
        return getSimpleResponse(true, value);
    }

    @POST
    @Path("postParams")
    @FormValid
    public Object postParams(@BeanParam Test test) {
        return getSimpleResponse(true, test);
    }

    @POST
    @Path("json")
    @FormValid
    public Result<Test> json(@NotBlank Test test) {
        return success(test);
    }

    @POST
    @Path("text")
    @Produces(MediaType.TEXT_PLAIN)
    public String text() {
        return "test";
    }

    @POST
    @Path("exception")
    public String exception() {
        throw new NullPointerException("异常");

    }
}