package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.core.utils.JsonUtils;
import com.scloudic.rabbitframework.example.web.biz.TestBiz;
import com.scloudic.rabbitframework.example.web.rest.ExmAbstractContextResource;
import com.scloudic.rabbitframework.example.web.rest.Test;
import com.scloudic.rabbitframework.web.Result;
import com.scloudic.rabbitframework.web.annotations.FormValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Singleton
@Path("/test")
public class TestResource extends ExmAbstractContextResource {
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
    public Object postParams(@FormParam("name") String name) {
        return getSimpleResponse(true, name);
    }

    @POST
    @Path("json")
    public Result<List<Test>> json(String name) {
        List<Test> list = new ArrayList<>();
        Test test = new Test();
        list.add(test);
        return success(list);
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