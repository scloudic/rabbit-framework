package com.rabbitframework.example.web.rest.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rabbitframework.example.web.rest.ExmAbstractContextResource;
import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.stereotype.Component;

import com.rabbitframework.web.AbstractContextResource;

@Component
@Singleton
@Path("/freemarker")
public class FreemarkerResource extends ExmAbstractContextResource {
	@GET
	@Path("html")
	@Produces(MediaType.TEXT_HTML)
	public Viewable freemarkerHtml() {
		return getFreemarker("/hello.html");
	}

	@GET
	@Path("ftl")
	@Produces(MediaType.TEXT_HTML)
	public Viewable freemarkerFtl() {
		return getFreemarker("/hello.ftl");
	}

	private Viewable getFreemarker(String path) {
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
