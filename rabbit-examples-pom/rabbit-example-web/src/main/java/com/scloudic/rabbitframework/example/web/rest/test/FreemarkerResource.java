package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.example.web.rest.ExmAbstractContextResource;
import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Singleton
@Path("/freemarker")
public class FreemarkerResource extends ExmAbstractContextResource {
    @GET
    @Path("html")
//	@Produces(MediaType.TEXT_HTML)
    //@UserAuthentication
    public Viewable freemarkerHtml(@Context HttpServletRequest request) {
        //System.out.println(CommonResponseUrl.getSys404ErrorUrl());
//        throw new BizException("dddd.no");
        return getFreemarker("/hello.html", request);
    }

    @GET
    @Path("ftl")
//	@Produces(MediaType.TEXT_HTML)
    // @UriPermissions
    public Viewable freemarkerFtl(@Context HttpServletRequest request) {
        return getFreemarker("/hello.ftl", request);
    }

    private Viewable getFreemarker(String path, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> lstValue = new ArrayList<String>();
        lstValue.add("item1");
        lstValue.add("item2");
        lstValue.add("item3");
        params.put("user", "Pavel");
        params.put("items", lstValue);
        return new Viewable(path, params);
    }
}
